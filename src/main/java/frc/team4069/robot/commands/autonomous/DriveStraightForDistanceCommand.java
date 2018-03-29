package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.Robot;
import frc.team4069.robot.io.Input;

public class DriveStraightForDistanceCommand extends CommandBase {

    private final double speed;
    private final int counterThreshold = 20;
    private final double acceptableError = 0.1;
    private final double derivativeMultiplier = 0.35;
    private int inRangeCounter = 0;
    private double distanceMeters;
    private double signedSpeed;
    private double initialPosition;
    private long startTime = 0;
    private long currentTime = 0;
    private long prevTime = currentTime;
    private double currentDistance = 0;
    private double prevDistance = currentDistance;
	
	private boolean exitCommand = false;

    public DriveStraightForDistanceCommand(double distanceMeters, double speed) {
        requires(driveBase);
        this.speed = speed;
        this.distanceMeters = distanceMeters;
        signedSpeed = distanceMeters > 0 ? speed : -speed;
    }

    @Override
    protected void initialize() {
        super.initialize();
        prevTime = currentTime = startTime = System.currentTimeMillis();
        initialPosition = driveBase.getDisplacementTraveledMeters();
        driveBase.driveContinuousSpeed(0, signedSpeed, true);
    }

    @Override
    protected void execute() {
		if(Robot.getOperatorControl() && (Math.abs(Input.getDriveSpeed()) > 0.1 || Math.abs(Input.getElevatorAxis()) > 0.1)){
			exitCommand = true;
		}
        prevDistance = currentDistance;
        currentDistance = driveBase.getDisplacementTraveledMeters();
        prevTime = currentTime;
        currentTime = System.currentTimeMillis();
        double metersPerSecond =
                (currentDistance - prevDistance) / ((double) (currentTime - prevTime) / 1000.0);
        double distance = driveBase.getDisplacementTraveledMeters() - initialPosition;
        double speedConstant = Math.abs(distanceMeters) * 3;
        double motorOutput = lerp(signedSpeed * speedConstant, 0, 0, distanceMeters, distance);
        if (distanceMeters < 0) {
            motorOutput -= metersPerSecond * derivativeMultiplier;
        } else {
            motorOutput -= metersPerSecond * derivativeMultiplier;
        }
        if (motorOutput > speed) {
            motorOutput = speed;
        } else if (motorOutput < -speed) {
            motorOutput = -speed;
        }
        driveBase.driveContinuousSpeed(0, motorOutput, true);
        if (distance >= distanceMeters - acceptableError
                && distance <= distanceMeters + acceptableError) {
            inRangeCounter++;
        } else {
            inRangeCounter = 0;
        }
        System.out.println("Meters per second: " + metersPerSecond);
        System.out.println("Delta time: " + (int) (currentTime - prevTime));
        System.out.println("Motor output: " + motorOutput);
        System.out.println("Distance: " + distance);
        System.out.println("Derivative factor: " + (metersPerSecond * derivativeMultiplier));
        System.out.println("Distance travelled: " + currentDistance);
        System.out.println("Distance meters: " + distanceMeters);
    }

    @Override
    protected boolean isFinished() {
		if(exitCommand){
			this.getGroup().cancel();
			return true;
		}
        return inRangeCounter >= counterThreshold;
    }

//    @Override
//    protected void end() {
//        driveBase.stop();
//    }

    private double lerp(double a, double b, double a2, double b2, double c) {
        double x = (c - a2) / (b2 - a2);
        return x * b + (1 - x) * a;
    }

}
