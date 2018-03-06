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
	
	public FollowSplinePathCommand(ArrayList<DoublePoint> points){
		requires(driveBase);
		this.points = points;
		spline = new SplinePathGenerator(0.8);
		spline.generateSpline(points, 90, 180, 250);
		leftPID = new PID(1, 0);
		rightPID = new PID(1, 0);
		gyroPID = new PID(1, 0);
		leftPID.setOutputCap(0.4);
		rightPID.setOutputCap(0.4);
		gyroPID.setOutputCap(0.6);
		leftPID.setTarget(spline.leftWheelIntegral[targetSplinePosition - 1]);
		rightPID.setTarget(spline.rightWheelIntegral[targetSplinePosition - 1]);
	}
	
	@Override
	protected void initialize(){
		startDistance = driveBase.getDistanceTraveledMeters();
		startDistanceLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel();
		startDistanceRightWheel = driveBase.getDistanceTraveledMetersRightWheel();
	}
	
	@Override
	protected void execute(){
		distanceTravelledMeters = driveBase.getDistanceTraveledMeters() - startDistance;
		distanceTravelledMetersLeftWheel = driveBase.getDistanceTraveledMetersLeftWheel() - startDistanceLeftWheel;
		distanceTravelledMetersRightWheel = driveBase.getDistanceTraveledMetersRightWheel() - startDistanceRightWheel;
		if(splinePosition < spline.splineIntegral.length - 1 && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
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
		return false;
	}
	
}