package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

public class DriveStraightForDistanceCommand extends CommandBase {

    private final double speed = 0.4;
	
	private int inRangeCounter = 0;
	private final int counterThreshold = 10;

	private final double acceptableError = 0.1;
	
    private double distanceMeters;
    private double signedSpeed;
    private double initialPosition;
	
	private long startTime = 0;
	
	private long currentTime = 0;
    private long prevTime = currentTime;
	
	private double currentDistance = 0;
	private double prevDistance = currentDistance;
	
	private final double derivativeMultiplier = 0.1;

    public DriveStraightForDistanceCommand(double distanceMeters) {
        requires(driveBase);
        this.distanceMeters = Math.abs(distanceMeters);
        signedSpeed = distanceMeters > 0 ? speed : -speed;
    }

    @Override
    protected void initialize() {
        super.initialize();
		prevTime = currentTime = startTime = System.currentTimeMillis();
        initialPosition = driveBase.getDistanceTraveledMeters();
        driveBase.driveContinuousSpeed(0, signedSpeed, true);
    }
	
	@Override
	protected void execute(){
		prevDistance = currentDistance;
        currentDistance = driveBase.getDistanceTraveledMeters();
        prevTime = currentTime;
        currentTime = System.currentTimeMillis();
        double metersPerSecond = (currentDistance - prevDistance) / ((double) (currentTime - prevTime) / 1000.0);
		double distance = Math.abs(initialPosition - driveBase.getDistanceTraveledMeters());
		double speedConstant = Math.abs(distanceMeters);
        double motorOutput = lerp(signedSpeed * speedConstant, 0, 0, distanceMeters, distance);
		if (distanceMeters < 0) {
            motorOutput -= metersPerSecond * derivativeMultiplier;
        } else {
            motorOutput += metersPerSecond * derivativeMultiplier;
        }
		if (motorOutput > speed) {
            motorOutput = speed;
        } else if (motorOutput < -speed) {
            motorOutput = -speed;
        }
		driveBase.driveContinuousSpeed(0, motorOutput, true);
		if (distance >= distanceMeters - acceptableError && distance <= distanceMeters + acceptableError) {
            inRangeCounter++;
        } else {
            inRangeCounter = 0;
        }
		System.out.println("Meters per second: " + metersPerSecond);
		System.out.println("Delta time: " + (int)(currentTime - prevTime));
		System.out.println("Motor output: " + motorOutput);
		System.out.println("Distance: " + currentDistance);
		
	}

    @Override
    protected boolean isFinished() {
        return inRangeCounter >= counterThreshold;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
	
	private double lerp(double a, double b, double a2, double b2, double c) {
        double x = (c - a2) / (b2 - a2);
        return x * b + (1 - x) * a;
    }
	
}
