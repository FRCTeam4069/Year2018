package frc.team4069.robot.commands.spline;

import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.spline.SplinePathGenerator;
import frc.team4069.robot.spline.DoublePoint;
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
	
	private int splinePosition = 0;
	
	private final int splinePositionFollowDistance = 10;
	
	private int targetSplinePosition = splinePositionFollowDistance;
	
	private int ticksWhileSplineFinished = 0;
	
	private double startAngle;
	private double angleAccumulator = 0.0;
	
	private double currentGyroscope = 0;
    private double prevGyroscope = currentGyroscope;
	
	public FollowSplinePathCommand(ArrayList<DoublePoint> points){
		requires(driveBase);
		this.points = points;
		spline = new SplinePathGenerator(0.8);
		spline.generateSpline(points, 90, 180, 0);
		leftPID = new PID(100, 0.1);
		rightPID = new PID(100, 0.1);
		gyroPID = new PID(1, 0);
		leftPID.setOutputCap(0.4);
		rightPID.setOutputCap(0.4);
		gyroPID.setOutputCap(0.6);
		leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
		rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
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
        /*double gyroAngle = getGyroAngle();
        if (gyroAngle - startAngle < -1 && this.turnRight) {
            gyroAngle += 360;
        } else if (gyroAngle - startAngle > 1 && !this.turnRight) {
            gyroAngle -= 360;
        }
        return gyroAngle;*/
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
		if(splinePosition == spline.splineIntegral.length - 1){
			ticksWhileSplineFinished++;
		}
		distanceTravelledMeters = driveBase.getDistanceTraveledMeters() - startDistance;
		distanceTravelledMetersLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel() - startDistanceLeftWheel;
		distanceTravelledMetersRightWheel = driveBase.getDistanceTraveledMetersRightWheel() - startDistanceRightWheel;
		while(splinePosition < spline.splineIntegral.length - 1 && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
			splinePosition++;
			targetSplinePosition = splinePosition + splinePositionFollowDistance;
			if(targetSplinePosition > spline.splineIntegral.length - 1){
				targetSplinePosition = spline.splineIntegral.length - 1;
			}
			leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
			rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
		}
		System.out.println("Left PID:\n-----");
		double leftWheelMotor = leftPID.getMotorOutput(distanceTravelledMetersLeftWheel);
		System.out.println("-----");
		System.out.println("Right PID:\n-----");
		double rightWheelMotor = rightPID.getMotorOutput(distanceTravelledMetersRightWheel);
		System.out.println("-----");
		driveBase.driveUnfiltered(leftWheelMotor, rightWheelMotor);
	}
	
	@Override
	public boolean isFinished(){
		return ticksWhileSplineFinished > 25;
	}
	
}