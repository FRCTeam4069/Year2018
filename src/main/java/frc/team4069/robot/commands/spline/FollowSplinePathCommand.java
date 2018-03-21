package frc.team4069.robot.commands.spline;

import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.spline.SplinePathGenerator;
import frc.team4069.robot.spline.DoublePoint;
import frc.team4069.robot.spline.Vector;
import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.motors.PID;
import java.util.ArrayList;

public class FollowSplinePathCommand extends CommandBase{
	
	private ArrayList<DoublePoint> points;
	
	private SplinePathGenerator spline;
	
	private PID leftPID, rightPID;
	
	private PID gyroPID;
	
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
	
	private double absoluteMotorSpeed = 0.6;
	
	public FollowSplinePathCommand(SplinePath path){
		requires(driveBase);
		this.points = points;
		spline = new SplinePathGenerator(0.55);
		spline.generateSpline(path);
		leftPID = new PID(100, 0.1);
		rightPID = new PID(100, 0.1);
		gyroPID = new PID(0.75, 0.2);
		/*leftPID.setOutputCap(0.4);
		rightPID.setOutputCap(0.4);
		gyroPID.setOutputCap(0.3);*/
		leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
		rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
		gyroPID.setTarget(spline.splineAngles[targetSplinePosition - 1]);
	}
	
	@Override
	protected void initialize(){
		startAngle = getGyroAngle();
		startDistance = driveBase.getDistanceTraveledMeters();
		startDistanceLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel();
		startDistanceRightWheel = driveBase.getDistanceTraveledMetersRightWheel();
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
		prevGyroscope = currentGyroscope;
        currentGyroscope = calculateGyroAngle();
		// Detect jump between 0 and 360 and adjust angle accumulator accordingly
        if (currentGyroscope - prevGyroscope > 180) {
            angleAccumulator -= 360.0;
        } else if (currentGyroscope - prevGyroscope < -180) {
            angleAccumulator += 360.0;
        }
		if(splinePosition == spline.leftWheel.length - 1){
			ticksWhileSplineFinished++;
		}
		distanceTravelledMeters = driveBase.getDistanceTraveledMeters() - startDistance;
		distanceTravelledMetersLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel() - startDistanceLeftWheel;
		distanceTravelledMetersRightWheel = driveBase.getDistanceTraveledMetersRightWheel() - startDistanceRightWheel;
		/*while(splinePosition < spline.splineIntegral.length - 1 && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
			splinePosition++;
			targetSplinePosition = splinePosition + splinePositionFollowDistance;
			if(targetSplinePosition > spline.splineIntegral.length - 1){
				targetSplinePosition = spline.splineIntegral.length - 1;
			}
			Vector followDirection = new Vector(new Vector(spline.leftWheel[targetSplinePosition - 1]).sub(new Vector(spline.leftWheel[targetSplinePosition - 2])).length(), new Vector(spline.rightWheel[targetSplinePosition - 1]).sub(new Vector(spline.rightWheel[targetSplinePosition - 2])).length()).normalize();
			followDirection = followDirection.multScalar(0.1);
			//leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
			//rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
			leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1] + followDirection.x);
			rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1] + followDirection.y);
		}*/
		while(splinePosition < spline.leftWheel.length - 1 && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
			splinePosition++;
		}
		//System.out.println("Spline position value: " + spline.pointsOnCurve[splinePosition].x + ", " + spline.pointsOnCurve[splinePosition].y);
		Vector followDirection = new Vector(new Vector(spline.leftWheel[splinePosition]).sub(new Vector(spline.leftWheel[splinePosition - 1])).length(), new Vector(spline.rightWheel[splinePosition]).sub(new Vector(spline.rightWheel[splinePosition - 1])).length()).normalize();
		followDirection = followDirection.multScalar(1);
		leftPID.setTarget(spline.leftWheelIntegral[splinePosition] + followDirection.x);
		rightPID.setTarget(spline.rightWheelIntegral[splinePosition] + followDirection.y);
		gyroPID.setTarget(spline.splineAngles[splinePosition]);
		if(gyroPID.logging){
			System.out.println("Gyro PID:\n-----");
		}
		double motorValues = gyroPID.getMotorOutput(-calculateGyroAngle());
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
		//Vector normalizedOutputs = new Vector(leftWheelMotor, rightWheelMotor).normalize().multScalar(0.6);
		Vector normalizedOutputs;
		double forwardVelocity = 3.0;
		if(distanceTravelledMeters < spline.splineIntegral[spline.splineIntegral.length - 1]){
			normalizedOutputs = new Vector(-motorValues + forwardVelocity, motorValues + forwardVelocity).normalize().multScalar(absoluteMotorSpeed);
		}
		else{
			normalizedOutputs = new Vector(-motorValues - forwardVelocity, motorValues - forwardVelocity).normalize().multScalar(absoluteMotorSpeed);
		}
		leftWheelMotor = normalizedOutputs.x;
		rightWheelMotor = normalizedOutputs.y;
		if(leftWheelMotor < 0){
			System.out.println(leftWheelMotor);
		}
		if(rightWheelMotor < 0){
			System.out.println(rightWheelMotor);
		}
		/*System.out.println("Left wheel motor: " + leftWheelMotor);
		System.out.println("Right wheel motor: " + rightWheelMotor);*/
		driveBase.driveUnfiltered(leftWheelMotor, rightWheelMotor);
	}
	
	@Override
	public boolean isFinished(){
		return ticksWhileSplineFinished > 70;
	}
	
}