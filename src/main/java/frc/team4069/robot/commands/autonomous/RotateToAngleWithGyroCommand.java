package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// Using the gyroscope, rotate the robot to a specific angle
public class RotateToAngleWithGyroCommand extends CommandBase {

    // Max turn speed
    private final double turnSpeedAbsolute = 0.3;
    // How many ticks does the gyroscope angle have to be in range for until the command finishes
    private final int counterThreshold = 50;
    // True if the robot is turning right
    private boolean turnRight;
    // Initial gyroscope angle
    private double startAngle;
    // Desired relative angle
    private double relativeAngle;
    // Gyroscope angle will be confined to +/- acceptableError degrees from the desired angle
    private double acceptableError = 1;
    // Counter for tracking how many ticks the gyroscope angle has been in the acceptable range of error
    private int inRangeCounter = 0;
    // Current and previous wheel positions, used for calculating derivative
    private double currentGyroscope = 0;
    private double prevGyroscope = currentGyroscope;

    // Current and previous times, used for calculating derivative
    private long currentTime = 0;
    private long prevTime = currentTime;

    // Lower derivativeMultiplier -> derivative has lesser effect on turning speed
    private double derivativeMultiplier = 0.0075;

    // Current speed at which the robot's wheels are turning
    private double turningSpeed;

    //Note: relativeAngle can be negative or positive angle
    public RotateToAngleWithGyroCommand(double relativeAngle) {
        requires(driveBase);
        this.relativeAngle = relativeAngle;
        if (this.relativeAngle > 180) {
            this.relativeAngle -= 360;
        } else if (this.relativeAngle < -180) {
            this.relativeAngle += 360;
        }
    }

    /**
     * Calculate relative gyro angle, accounting for jump from 360 to 0
     */
    private double calculateGyroAngle() {
        double gyroAngle = getGyroAngle();
        if (gyroAngle - startAngle < -1 && this.turnRight) {
            gyroAngle += 360;
        } else if (gyroAngle - startAngle > 1 && !this.turnRight) {
            gyroAngle -= 360;
        }
        return gyroAngle;
    }

    /**
     * Calculate degrees turned from starting angle
     */
    private double calculateDelta() {
        double gyroAngle = calculateGyroAngle();

        return gyroAngle - startAngle;
    }

    protected void initialize() {
        startAngle = getGyroAngle();
        // If passed angle to turn is positive, turn right
        turnRight = relativeAngle > 0;
        prevGyroscope = currentGyroscope = getGyroAngle();
        prevTime = currentTime = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        prevGyroscope = currentGyroscope;
        currentGyroscope = calculateGyroAngle();
        prevTime = currentTime;
        currentTime = System.currentTimeMillis();
        System.out.println("Delta time: " + (int) (currentTime - prevTime));
        double delta = calculateDelta();
        double gyroAngle = calculateGyroAngle();
		double degPerSecond = (currentGyroscope - prevGyroscope) / ((double)(currentTime - prevTime) / 1000.0);
		System.out.println("Degrees per second: " + degPerSecond);
        // The constant has the effect of narrowing the linearInterpolation to a small range around the desired angle and keeping motor output to a max everywhere else
        double speedConstant = Math.abs(relativeAngle) * (1.0 / 6);
        double motorOutput = lerp(turnSpeedAbsolute * speedConstant, 0, 0, relativeAngle,
                gyroAngle - startAngle);
        System.out.println("Start gyroscope position: " + startAngle + ", current gyroscope position: " + gyroAngle);
		System.out.println("Gyro delta: " + delta);
		System.out.println("Derivative: " + (degPerSecond * derivativeMultiplier));
        motorOutput += degPerSecond * derivativeMultiplier;
        // Restrict speed to +/- turnSpeedAbsolute
        if (motorOutput > turnSpeedAbsolute) {
            motorOutput = turnSpeedAbsolute;
        } else if (motorOutput < -turnSpeedAbsolute) {
            motorOutput = -turnSpeedAbsolute;
        }
        System.out.println("Final motor output: " + motorOutput);
        driveBase.rotate(turnRight ? motorOutput : -motorOutput);
        // If robot is in range of acceptable error, increment the in range counter, otherwise zero it
        if (delta >= relativeAngle - acceptableError && delta <= relativeAngle + acceptableError) {
            inRangeCounter++;
        } else {
            inRangeCounter = 0;
        }
    }

    protected boolean isFinished() {
        // Turning is complete once robot has been within the acceptable degree of error for counterThreshold ticks
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
