package frc.team4069.robot.util;

public class PIDdle {

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
    private double lastError;

    private double mKP, mKI, mKD;

    private long mMinimumTimeBetweenComputeCalls = 1000; // 1 sec if called < this value, will return and wait till at least
    //this amount of time has passed before computing next value

    private double mOutputMinimumValue = Double.MIN_VALUE;

    private double mOutputMaximumValue = Double.MAX_VALUE;

    private boolean mAutomaticMode = false;


    /**
     * Compute : Called as frequently as possible with current input value to PID
     *
     * @param currentInput - input value to PID
     * @return PID output value
     */
    public double compute(double currentInput) {
        if (!mAutomaticMode) {
            return mPIDOutput;
        }

        mCurrentInput = currentInput;  //remember for when re-enabling after disable

        long now = System.currentTimeMillis();

        long deltaTime = (now - mLastTimeCalled);

        if (deltaTime >= mMinimumTimeBetweenComputeCalls) {
            double error = mDesiredOutput
                    - currentInput; // Get error between current Input and wanted Setpoint

            mIntegralAccumulator += (mKI * error); // Accumulate the integral term

            if (mIntegralAccumulator > mOutputMaximumValue) {
                mIntegralAccumulator = mOutputMaximumValue;
            } else if (mIntegralAccumulator < mOutputMinimumValue) {
                mIntegralAccumulator = mOutputMinimumValue;
            }

            double derivativeError = lastError - error; // Compute the derivative of the error

            mPIDOutput = mKP * error + mIntegralAccumulator + mKD * derivativeError;

            // Now clamp the output
            if (mPIDOutput > mOutputMaximumValue) {
                mPIDOutput = mOutputMaximumValue;
            } else if (mPIDOutput < mOutputMinimumValue) {
                mPIDOutput = mOutputMinimumValue;
            }

            /* Remember some variables for next time */

            lastError = error;
            mLastInputValue = currentInput;
            mLastTimeCalled = now;
        } // if enough time has passed

        return mPIDOutput;
    }

    /**
     * getCurrentOutput
     *
     * @return current PID output value
     */
    double getCurrentOutput() {
        return mPIDOutput;
    } // SetTunings

    public double getP() {
        return mKP;
    }

    public double getI() {
        return mKI;
    }

    public double getD() {
        return mKD;
    }

    /**
     * getTunings : Kp,Ki,kD parameters, NOTE NEVER send negative values for these.  If you want the PID
     * to run with negative outputs, call the setDirection with PIDDLE_REVERSE
     *
     * @param kP : proportional constant
     * @param kI : integral constant
     * @param kD : Derivative constant
     */
    public void setTunings(double kP, double kI, double kD) {
        if (kP < 0 || kI < 0 || kD < 0) {
            return;
        }

        double SampleTimeInSec = ((double) mMinimumTimeBetweenComputeCalls) / 1000;
        mKP = kP;
        mKI = kI * SampleTimeInSec;
        mKD = kD / SampleTimeInSec;

        // if pid values will be negative, adjust params to be negative
        if (controllerDirection == PIDDLE_REVERSE) {
            mKP = (0 - mKP);
            mKI = (0 - mKI);
            mKD = (0 - mKD);
        }
    } // setTunings

    /**
     * setMinimumSampleTime : New minimum sample time in milliseconds
     * if compute() is called faster than this, it will do nothing until at LEAST this amount of
     * time has passed.
     *
     * @param newSampleTime : new minimum sample time in milliseconds
     */
    public void setMinimumSampleTime(long newSampleTime) {
        if (newSampleTime > 0) {
            double ratio = (double) newSampleTime / (double) mMinimumTimeBetweenComputeCalls;

            mKI *= ratio;  //scale I&D so we don't get a 'kick' when sample time changes
            mKD /= ratio;

            mMinimumTimeBetweenComputeCalls = newSampleTime;
        }
    }

    /**
     * SetOutputLimits : It is STRONGLY encouraged that you set these to reasonable values.
     *
     * @param min minimum output value allowed/wanted by PID control
     * @param max maximum output value allowed/wanted by PID control
     */
    public void setOutputLimits(double min, double max) {
        if (min > max) {
            return;
        }

        mOutputMinimumValue = min;
        mOutputMaximumValue = max;

        if (mPIDOutput > mOutputMaximumValue) {
            mPIDOutput = mOutputMaximumValue;
        } else if (mPIDOutput < mOutputMinimumValue) {
            mPIDOutput = mOutputMinimumValue;
        }

        if (mIntegralAccumulator > mOutputMaximumValue) {
            mIntegralAccumulator = mOutputMaximumValue;
        } else if (mIntegralAccumulator < mOutputMinimumValue) {
            mIntegralAccumulator = mOutputMinimumValue;
        }
    }

    /**
     * SetMode : Set the PID controller to PIDDLE_AUTOMATIC or PIDDLE_MANUAL
     * in Manual mode compute() will do nothing.
     *
     * @param mode PIDDLE_AUTOMATIC or PIDDLE_MANUAL
     */
    public void setMode(int mode) {
        boolean newAuto = (mode == PIDDLE_AUTOMATIC);

        if (newAuto == !mAutomaticMode) {
            initialize();
        }
        mAutomaticMode = newAuto;
    }

    /**
     * initialize : This is called by SetMode when Automatic is enabled to resume PID operations without
     * hopefully any 'jumps' or 'kicks'.  It is not intended to be called from outside the class
     * (thus the private designation)
     */
    private void initialize() {
        mLastInputValue = mCurrentInput;
        mIntegralAccumulator = mPIDOutput;

        if (mIntegralAccumulator > mOutputMaximumValue) {
            mIntegralAccumulator = mOutputMaximumValue;
        } else if (mIntegralAccumulator < mOutputMinimumValue) {
            mIntegralAccumulator = mOutputMinimumValue;
        }
    }

    /**
     * setControllerDirection : If the PID output should be positive PIDDLE_DIRECT should be set
     * if the PID output is meant to be negative, PIDDLE_REVERSE should be set.
     * This parameter auto-sets the tuning parameters to negative, NEVER send negative tuning values to
     * kI,kP,kD
     */
    public void setControllerDirection(int direction) {
        controllerDirection = direction;
    }
}
