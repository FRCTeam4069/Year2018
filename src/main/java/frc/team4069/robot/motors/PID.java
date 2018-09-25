package frc.team4069.robot.motors;

public class PID{
	
	private double kP, kI, kD;
	
	private double target;
	
	private double prevPosition;
	
	private boolean firstTick = true;
	
	private long currentTime, prevTime;
	
	private double deltaTime = 0.0;
	
	private double outputCap = 1.0;
	
	private boolean outputCapSet = false;
	
	public boolean logging = false;
	
	private double integral = 0;
	
	public PID(double kP, double kI, double kD){
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		currentTime = prevTime = System.currentTimeMillis();
	}
	
	public void setTarget(double target){
		this.target = target;
	}
	
	public void setOutputCap(double outputCap){
		this.outputCap = outputCap;
		this.outputCapSet = true;
	}
	
	public double getMotorOutput(double position){
		if(logging){
			System.out.println("Position: " + position);
			System.out.println("Target position: " + target);
		}
		prevTime = currentTime;
		currentTime = System.currentTimeMillis();
		deltaTime = (int)(currentTime - prevTime) / 1000.0;
		if(logging){
			System.out.println("Delta time: " + deltaTime);
		}
		double proportional = kP * (target - position);
		if(logging){
			System.out.println("Proportional: " + proportional);
		}
		double derivative = 0;
		if(firstTick){
			firstTick = false;
		}
		else{
			derivative = kD * -((position - prevPosition) / deltaTime);
		}
		if(logging){
			System.out.println("Derivative: " + derivative);
		}
		integral += kI * deltaTime * (target - position);
		double motorOutput = proportional + integral + derivative;
		if(outputCapSet){
			if(motorOutput > outputCap){
				motorOutput = outputCap;
			}
			else if(motorOutput < -outputCap){
				motorOutput = -outputCap;
			}
		}
		if(logging){
			System.out.println("Motor output: " + motorOutput);
		}
		prevPosition = position;
		return motorOutput;
	}
	
}