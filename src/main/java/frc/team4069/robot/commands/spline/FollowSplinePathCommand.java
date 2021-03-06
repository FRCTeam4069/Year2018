package frc.team4069.robot.commands.spline;

import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.spline.SplinePathGenerator;
import frc.team4069.robot.spline.DoublePoint;
import frc.team4069.robot.spline.Vector;
import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.motors.PID;
import frc.team4069.robot.io.Input;
import frc.team4069.robot.Robot;
import java.util.ArrayList;

public class FollowSplinePathCommand extends CommandBase{
	
	private ArrayList<DoublePoint> points;
	
	private SplinePathGenerator spline;
	
	private PID leftPID, rightPID;
	
	private PID gyroPID;
	
	private PID distancePID;
	
	private double distanceTravelledMeters;
	private double distanceTravelledMetersLeftWheel, distanceTravelledMetersRightWheel;
	
	private double startDistance;
	private double startDistanceLeftWheel, startDistanceRightWheel;
	
	private int splinePosition = 1;
	
	private final int splinePositionFollowDistance = 5;
	
	private int targetSplinePosition = splinePositionFollowDistance;
	
	private int ticksWhileSplineFinished = 0;
	
	private double startAngle;
	private double angleAccumulator = 0.0;
	
	private double currentGyroscope = 0;
    private double prevGyroscope = currentGyroscope;
	
	private double absoluteMotorSpeed = 0.7;
	
	private double splineAngleAccumulator = 0.0;
	
	private double forwardVelocityCap = 3.0;
	
	private int splineFinishedCounter = 25;

	private double terminationPoint = 1.0;

	private boolean exitCommand = false;
	
	public FollowSplinePathCommand(SplinePath path){
		requires(driveBase);
		System.out.println("FOLLOW SPLINE PATH COMMAND START");
		if(path != null){
			spline = path.getSpline();
			leftPID = new PID(100, 0.0, 0.1);
			rightPID = new PID(100, 0.0, 0.1);
			gyroPID = new PID(0.5, 0.0, 0.1);
			distancePID = new PID(15, 0.0, 1.0);
			distancePID.setOutputCap(Math.abs(forwardVelocityCap));
			leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
			rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
			gyroPID.setTarget(spline.splineAngles[targetSplinePosition - 1]);
			distancePID.setTarget(spline.splineIntegral[(int)((spline.splineIntegral.length - 1) * terminationPoint)]);
		}
		else{
			exitCommand = true;
		}
	}
	
	public FollowSplinePathCommand(SplinePath path, int splineFinishedCounter, double derivative, double speed){
		requires(driveBase);
		System.out.println("FOLLOW SPLINE PATH COMMAND START");
		if(path != null){
			absoluteMotorSpeed = speed;
			this.splineFinishedCounter = splineFinishedCounter;
			spline = path.getSpline();
			leftPID = new PID(100, 0.0, 0.1);
			rightPID = new PID(100, 0.0, 0.1);
			gyroPID = new PID(0.5, 0.0, 0.1);
			distancePID = new PID(15, 0.0, derivative);
			distancePID.setOutputCap(Math.abs(forwardVelocityCap));
			leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
			rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
			gyroPID.setTarget(spline.splineAngles[targetSplinePosition - 1]);
			distancePID.setTarget(spline.splineIntegral[spline.splineIntegral.length - 1]);
		}
		else{
			exitCommand = true;
		}
	}
	
	public FollowSplinePathCommand(SplinePath path, int splineFinishedCounter){
		this(path);
		this.splineFinishedCounter = splineFinishedCounter;
	}
	
	@Override
	protected void initialize(){
		startAngle = getGyroAngle();
		startDistance = driveBase.getDisplacementTraveledMeters();
		startDistanceLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel();
		startDistanceRightWheel = driveBase.getDistanceTraveledMetersRightWheel();
		splinePosition = 1;
		targetSplinePosition = splinePositionFollowDistance;
		ticksWhileSplineFinished = 0;
		angleAccumulator = 0;
		splineAngleAccumulator = 0.0;
	}
	
	 /**
     * Calculate relative gyro angle, accounting for jump from 360 to 0
     */
    private double calculateGyroAngle() {
        double gyroAngle = getGyroAngle();
        return gyroAngle + angleAccumulator;
    }

    /**
     * Calculate degrees turned from starting angle
     */
    private double calculateDelta() {
        double gyroAngle = calculateGyroAngle();
        return gyroAngle - startAngle;
    }
	
	@Override
	protected void execute(){
    	System.out.println("FOLLOWING SPLINE");
		prevGyroscope = currentGyroscope;
        currentGyroscope = calculateDelta();
		if(Robot.getOperatorControl() && (Math.abs(Input.getDriveSpeed()) > 0.1 || Math.abs(Input.getElevatorAxis()) > 0.1)){
			exitCommand = true;
		}
		// Detect jump between 0 and 360 and adjust angle accumulator accordingly
        if (currentGyroscope - prevGyroscope > 180) {
            angleAccumulator -= 360.0;
        } else if (currentGyroscope - prevGyroscope < -180) {
            angleAccumulator += 360.0;
        }
		if(splinePosition == (int)((spline.leftWheel.length - 1) * terminationPoint)){
			ticksWhileSplineFinished++;
		}
		distanceTravelledMeters = driveBase.getDisplacementTraveledMeters() - startDistance;
		distanceTravelledMetersLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel() - startDistanceLeftWheel;
		distanceTravelledMetersRightWheel = driveBase.getDistanceTraveledMetersRightWheel() - startDistanceRightWheel;
		while(splinePosition < (int)((spline.leftWheel.length - 1) * terminationPoint) && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
			splinePosition++;
			if(spline.splineAngles[splinePosition] - spline.splineAngles[splinePosition - 1] >= 180.0){
				splineAngleAccumulator -= 360.0;
			}
			else if(spline.splineAngles[splinePosition] - spline.splineAngles[splinePosition - 1] <= -180.0){
				splineAngleAccumulator += 360.0;
			}
		}
		System.out.println("Distance travelled: " + distanceTravelledMeters);
		//System.out.println("Spline angle: " + (spline.splineAngles[splinePosition] + splineAngleAccumulator));
		System.out.println("Spline position: " + spline.pointsOnCurve[splinePosition].x + ", " + spline.pointsOnCurve[splinePosition].y);
		//System.out.println("Calculated spline angle: " + Math.toDegrees(Math.atan2(spline.pointsOnCurve[splinePosition].y - spline.pointsOnCurve[splinePosition - 1].y, spline.pointsOnCurve[splinePosition].x - spline.pointsOnCurve[splinePosition - 1].x)));
		Vector followDirection = new Vector(new Vector(spline.leftWheel[splinePosition]).sub(new Vector(spline.leftWheel[splinePosition - 1])).length(), new Vector(spline.rightWheel[splinePosition]).sub(new Vector(spline.rightWheel[splinePosition - 1])).length()).normalize();
		followDirection = followDirection.multScalar(1);
		leftPID.setTarget(spline.leftWheelIntegral[splinePosition] + followDirection.x);
		rightPID.setTarget(spline.rightWheelIntegral[splinePosition] + followDirection.y);
		gyroPID.setTarget(spline.splineAngles[splinePosition] + splineAngleAccumulator);
		if(gyroPID.logging){
			System.out.println("Gyro PID:\n-----");
		}
		double motorValues = gyroPID.getMotorOutput(-calculateDelta());
		if(gyroPID.logging){
			System.out.println("-----");
		}
		if(leftPID.logging){
			System.out.println("Left PID:\n-----");
		}
		double leftWheelMotor = (leftPID.getMotorOutput(distanceTravelledMetersLeftWheel) - motorValues) / 2;
		if(leftPID.logging){
			System.out.println("-----");
		}
		if(rightPID.logging){
			System.out.println("Right PID:\n-----");
		}
		double rightWheelMotor = (rightPID.getMotorOutput(distanceTravelledMetersRightWheel) + motorValues) / 2;
		if(rightPID.logging){
			System.out.println("-----");
		}
		double forwardVelocity = distancePID.getMotorOutput(distanceTravelledMeters);
		double velocityRatio = forwardVelocity / forwardVelocityCap;
		if(velocityRatio < 0){
			motorValues *= -1;
		}
		Vector normalizedOutputs = new Vector(-motorValues + forwardVelocityCap, motorValues + forwardVelocityCap).normalize().multScalar(absoluteMotorSpeed * (forwardVelocity / forwardVelocityCap));
		leftWheelMotor = normalizedOutputs.x;
		rightWheelMotor = normalizedOutputs.y;
		System.out.println("Forward velocity cap: " + forwardVelocityCap);
		System.out.println("Normalized outputs: " + leftWheelMotor + ", " + rightWheelMotor);
		System.out.println("motorValues: " + motorValues);
		if(leftWheelMotor > 1){
			leftWheelMotor = 1;
		}
		else if(leftWheelMotor < -1){
			leftWheelMotor = -1;
		}
		if(rightWheelMotor > 1){
			rightWheelMotor = 1;
		}
		else if(rightWheelMotor < -1){
			rightWheelMotor = -1;
		}
		driveBase.driveUnfiltered(leftWheelMotor, rightWheelMotor);
	}

	public CommandBase terminate(double terminationPoint){
    	this.terminationPoint = terminationPoint;
    	return this;
	}
	
	@Override
	public boolean isFinished(){
		if(exitCommand){
			exitCommand = false;
			//this.getGroup().cancel();
			return true;
		}
		return ticksWhileSplineFinished > splineFinishedCounter;
	}
	
}