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
	
	// Generates the spline used in this command
	private SplinePathGenerator spline;
	
	// PID for controlling the angle of the robot
	private PID gyroPID;
	
	// PID for slowing the robot down at the end of the spline
	private PID distancePID;
	
	// Distance travelled by the center of the robot in meters
	private double distanceTravelledMeters;
	
	// Initial distance travelled of the robot
	private double startDistance;
	
	// Position on the spline closest to where the robot is
	private int splinePosition = 1;
	
	// Number of times execute has been called while the robot is at the end of the spline
	private int ticksWhileSplineFinished = 0;
	
	// Initial gyroscope angle
	private double startAngle;
	
	private double angleAccumulator = 0.0;
	
	private double currentGyroscope = 0;
    private double prevGyroscope = currentGyroscope;
	
	private double absoluteMotorSpeed = 0.6;
	
	private double splineAngleAccumulator = 0.0;
	
	private double forwardVelocityCap = 1.0;
	
	public FollowSplinePathCommand(SplinePath path){
		requires(driveBase);
		spline = new SplinePathGenerator(0.55);
		spline.generateSpline(path);
		gyroPID = new PID(0.75, 0.2);
		distancePID = new PID(15, 1.0);
		distancePID.setOutputCap(forwardVelocityCap);
		gyroPID.setTarget(spline.splineAngles[splinePosition]);
		distancePID.setTarget(spline.splineIntegral[spline.splineIntegral.length - 1]);
	}
	
	@Override
	protected void initialize(){
		System.out.println("FOLLOW SPLINE PATH COMMAND INITIALIZE");
		// Record initial gyro angle and distance travelled
		startAngle = getGyroAngle();
		startDistance = driveBase.getDistanceTraveledMeters();
	}
	
	@Override
	protected void execute(){
		// Update current and previous gyroscope angles
		prevGyroscope = currentGyroscope;
        currentGyroscope = getGyroAngle() + angleAccumulator;
		// Detect jump between 0 and 360 and adjust angle accumulator accordingly
        if (currentGyroscope - prevGyroscope > 180) {
            angleAccumulator -= 360.0;
        } else if (currentGyroscope - prevGyroscope < -180) {
            angleAccumulator += 360.0;
        }
		// If the robot has reached the end of the spline, increment the ticksWhileSplineFinished counter
		if(splinePosition == spline.leftWheel.length - 1){
			ticksWhileSplineFinished++;
		}
		// Calculate the distance travelled by the robot from its start position
		distanceTravelledMeters = driveBase.getDistanceTraveledMeters() - startDistance;
		// Increment the spline position tracker until it reaches the point closest behind where the robot is
		while(splinePosition < spline.leftWheel.length - 1 && distanceTravelledMeters >= spline.splineIntegral[splinePosition]){
			splinePosition++;
			// Use an angle accumulator for the spline angle array to account for the possible jump from 0 to 360
			if(spline.splineAngles[splinePosition] - spline.splineAngles[splinePosition - 1] >= 180.0){
				splineAngleAccumulator -= 360.0;
			}
			else if(spline.splineAngles[splinePosition] - spline.splineAngles[splinePosition - 1] <= -180.0){
				splineAngleAccumulator += 360.0;
			}
		}
		// Set the target of the gyro PID to the desired angle for the robot's position on the spline
		gyroPID.setTarget(spline.splineAngles[splinePosition] + splineAngleAccumulator);
		// Calculate the gyroPID's desired motor outputs to turn the robot from the gyroscope's current angle to the gyroPID's target angle
		double motorValues = gyroPID.getMotorOutput(-currentGyroscope);
		// This is effectively equal to forwardVelocityCap for most of the spline up until around the end, where the distancePID calculates the optimal velocity to smoothly stop the robot
		double forwardVelocity = distancePID.getMotorOutput(distanceTravelledMeters);
		// If the robot is trying to drive backwards, the wheels need to move in opposite directions to reach the target angle
		if(forwardVelocity < 0){
			motorValues *= -1;
		}
		// normalizedOutputs is a vector representing the left wheel motor outputs (x) and right wheel (y)
		// Vectors are useful for this as they can be normalized to ensure the total motor output "distance" is constant
		// forwardVelocityCap is added to both x and y gyroPID desired outputs to maintain a constant forward velocity in the normalized vector
		// Final vector is the product of absoluteMotorSpeed and forwardVelocity (capped at 1.0), to scale the motor output "distance" down
		Vector normalizedOutputs = new Vector(-motorValues + forwardVelocityCap, motorValues + forwardVelocityCap).normalize().multScalar(absoluteMotorSpeed * forwardVelocity);
		// Drive using the calculated motor outputs
		driveBase.driveUnfiltered(normalizedOutputs.x, normalizedOutputs.y);
	}
	
	@Override
	public boolean isFinished(){
		// End the command 70 ticks after the robot has reached the end to allow it time to slow down
		return ticksWhileSplineFinished > 70;
	}
	
}