package frc.team4069.robot.util;

public class PIDdle
{
  public static final int PIDDLE_MANUAL = 0;
  public static final int PIDDLE_AUTOMATIC = 1;
  public static final int PIDDLE_DIRECT = 0;
  public static final int PIDDLE_REVERSE = 1;

  private int controllerDirection = PIDDLE_DIRECT;

  /* working variables */
  private long mLastTimeCalled;

  private double mCurrentInput;
 
  private double mPIDOutput;

  private double mDesiredOutput;
  
  private double mIntegralAccumulator;
  private double mLastInputValue;
  
  private double mKP, mKI, mKD;
  
  private long mMinimumTimeBetweenComputeCalls = 1000; // 1 sec if called < this value, will return and wait till at least
                                       //this amount of time has passed before computing next value
  
  private double mOutputMinimumValue = Double.MIN_VALUE;
  
  private double mOutputMaximumValue=Double.MAX_VALUE;
  
  boolean mAutomaticMode = false;

  
  /**
   * Compute : Called as frequently as possible with current input value to PID
   * 
   * 
   * @param CurrentInput - input value to PID
   * @return PID output value
   */
  
  public double Compute(double CurrentInput)
  {
    if (!mAutomaticMode)
      return mPIDOutput;
    
    mCurrentInput = CurrentInput;  //remember for when re-enabling after disable
    
    long now = System.currentTimeMillis();

    long deltaTime = (now - mLastTimeCalled);

    if (deltaTime >= mMinimumTimeBetweenComputeCalls)
    {
      double error = mDesiredOutput - CurrentInput; // Get error between current Input and wanted Setpoint

      mIntegralAccumulator += (mKI * error); // Accumulate the integral term

      if (mIntegralAccumulator > mOutputMaximumValue)
        mIntegralAccumulator = mOutputMaximumValue;
      else
        if (mIntegralAccumulator < mOutputMinimumValue)
          mIntegralAccumulator = mOutputMinimumValue;

      double derivativeInput = (CurrentInput - mLastInputValue); // compute derivative input from last call

      
      mPIDOutput = mKP * error + mIntegralAccumulator - mKD * derivativeInput;

      // Now clamp the output
      if (mPIDOutput > mOutputMaximumValue)
        mPIDOutput = mOutputMaximumValue;
      else
        if (mPIDOutput < mOutputMinimumValue)
          mPIDOutput = mOutputMinimumValue;

      /* Remember some variables for next time */

      mLastInputValue = CurrentInput;
      mLastTimeCalled = now;
    } // if enough time has passed
    
    return mPIDOutput;
  } // Compute


  /**
   * GetCurrentOutput 
   * @return current PID output value
   */
  double GetCurrentOutput()
  {
    return mPIDOutput;
  }
  
  
  
  /**
   * SetTunings : Kp,Ki,kD parameters, NOTE NEVER send negative values for these.  If you want the PID
   * to run with negative outputs, call the setDirection with PIDDLE_REVERSE
   * 
   * @param Kp  : proportional constant
   * @param Ki  : integral constant
   * @param Kd  : Derivative constant
   */
  
  
  public void SetTunings(double Kp, double Ki, double Kd)
  {
    if (Kp < 0 || Ki < 0 || Kd < 0)
      return;

    double SampleTimeInSec = ((double) mMinimumTimeBetweenComputeCalls) / 1000;
    mKP = Kp;
    mKI = Ki * SampleTimeInSec;
    mKD = Kd / SampleTimeInSec;

    // if pid values will be negative, adjust params to be negative
    if (controllerDirection == PIDDLE_REVERSE)
    {
      mKP = (0 - mKP);
      mKI = (0 - mKI);
      mKD = (0 - mKD);
    }
  } // SetTunings

  
  /**
   * SetMinimumSampleTime : New minimum sample time in milliseconds
   * if compute() is called faster than this, it will do nothing until at LEAST this amount of
   * time has passed.
   * @param NewSampleTime : new minimum sample time in milliseconds
   */
  public void SetMinimumSampleTime(long NewSampleTime)
  {
    if (NewSampleTime > 0)
    {
      double ratio = (double) NewSampleTime / (double) mMinimumTimeBetweenComputeCalls;

      mKI *= ratio;  //scale I&D so we don't get a 'kick' when sample time changes
      mKD /= ratio;

      mMinimumTimeBetweenComputeCalls = NewSampleTime;
    }
  } // SetSampleTime


  /**
   * SetOutputLimits : It is STRONGLY encouraged that you set these to reasonable values.
   * 
   * @param Min : minimum output value allowed/wanted by PID control
   * @param Max : maximum output value allowed/wanted by PID control
   */
  public void SetOutputLimits(double Min, double Max)
  {
    if (Min > Max)
      return;

    mOutputMinimumValue = Min;
    mOutputMaximumValue = Max;

    if (mPIDOutput > mOutputMaximumValue)
      mPIDOutput = mOutputMaximumValue;
    else
      if (mPIDOutput < mOutputMinimumValue)
        mPIDOutput = mOutputMinimumValue;
    
    if (mIntegralAccumulator > mOutputMaximumValue)
      mIntegralAccumulator = mOutputMaximumValue;
    else
      if (mIntegralAccumulator < mOutputMinimumValue)
        mIntegralAccumulator = mOutputMinimumValue;
  }// SetOutputLimits

  
  /**
   * SetMode : Set the PID controller to PIDDLE_AUTOMATIC or PIDDLE_MANUAL
   *  in Manual mode compute() will do nothing.
   *  
   * @param Mode  PIDDLE_AUTOMATIC or PIDDLE_MANUAL
   */
  
  public void SetMode(int Mode)
  {
    boolean newAuto = (Mode == PIDDLE_AUTOMATIC);

    if (newAuto == !mAutomaticMode)
    { 
      Initialize();
    }
    mAutomaticMode = newAuto;
  } // SetMode

  
  
  /**
   * Initialize : This is called by SetMode when Automatic is enabled to resume PID operations without
   * hopefully any 'jumps' or 'kicks'.  It is not intended to be called from outside the class
   *  (thus the private designation)
   */
  
  private void Initialize()
  {
    mLastInputValue = mCurrentInput;
    mIntegralAccumulator = mPIDOutput;
    
    if (mIntegralAccumulator > mOutputMaximumValue)
      mIntegralAccumulator = mOutputMaximumValue;
    else
      if (mIntegralAccumulator < mOutputMinimumValue)
        mIntegralAccumulator = mOutputMinimumValue;
  } // Initialize

  
  /**
   * SetControllerDirection : If the PID output should be positive PIDDLE_DIRECT should be set
   * if the PID output is meant to be negative, PIDDLE_REVERSE should be set.
   * This parameter auto-sets the tuning parameters to negative, NEVER send negative tuning values to
   * kI,kP,kD
   * @param Direction
   */
  
  public void SetControllerDirection(int Direction)
  {
    controllerDirection = Direction;
  } // SetControllerDirection
} // class PIDdle
