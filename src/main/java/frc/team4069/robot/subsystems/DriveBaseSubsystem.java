package frc.team4069.robot.subsystems;

import frc.team4069.robot.commands.OperatorDriveCommand;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.DriveEncoder;
import frc.team4069.robot.motors.TalonSRXMotor;
import frc.team4069.robot.util.LowPassFilter;

// A class that manages all hardware components of the drive base and provides utility functions
// for instructing it to drive and turn in a variety of ways
public class DriveBaseSubsystem extends SubsystemBase {

    // The lateral distance between the robot's wheels in meters
    public static final double ROBOT_TRACK_WIDTH_METERS = 0.6;
    public static final double SLOW_SPEED = 0.2;
    // A singleton instance of the drive base subsystem
    private static DriveBaseSubsystem instance;
    // The number of meters each wheel travels per motor rotation
    private final double METERS_PER_ROTATION = 0.61;
    private final int ENCODER_TICKS_PER_ROTATION = 256;
    // Toggleable precision mode by driver
    private boolean precision = false;
    // Left and right drive motors
    private TalonSRXMotor leftDrive;
    private TalonSRXMotor rightDrive;
    // Left and right drive encoders;
    private DriveEncoder leftEncoder;
    private DriveEncoder rightEncoder;
    // Low pass filters that smooth steering
    private LowPassFilter leftSideLpf;
    private LowPassFilter rightSideLpf;

    // Initialize the drive motors
    private DriveBaseSubsystem() {
        // Initialize the motors with predefined port numbers
        leftDrive = new TalonSRXMotor(IOMapping.LEFT_DRIVE_CAN_BUS, ENCODER_TICKS_PER_ROTATION,
                false, 11, 13);
        rightDrive = new TalonSRXMotor(IOMapping.RIGHT_DRIVE_CAN_BUS, ENCODER_TICKS_PER_ROTATION,
                false, 18, 20);
        // Initialize the encoders with predefined port numbers
        leftEncoder = new DriveEncoder(0, 1);
        rightEncoder = new DriveEncoder(8, 9);
        // Initialize the low pass filters with a time period of 200 milliseconds
        leftSideLpf = new LowPassFilter(200);
        rightSideLpf = new LowPassFilter(200);
    }

    // A public getter for the instance
    public static DriveBaseSubsystem getInstance() {
        // If the instance is null, create a new one
        if (instance == null) {
            instance = new DriveBaseSubsystem();
        }

        return instance;
    }

    // A public getter for the distance traveled in meters
    public double getDistanceTraveledMeters() {
        // Get the absolute values of the positions of each of the motors and calculate the average
        double leftWheelRotationsTraveled =
                Math.abs(leftEncoder.getDistanceTraveledRotations());
        double rightWheelRotationsTraveled =
                Math.abs(rightEncoder.getDistanceTraveledRotations());
        double averageRotationsTraveled =
                (leftWheelRotationsTraveled + rightWheelRotationsTraveled) / 2;
        System.out.println(leftEncoder.get());
        // Multiply the average rotations by the number of wheels per rotation to get the average
        // distance traveled in meters
        return averageRotationsTraveled * METERS_PER_ROTATION;
    }

    // Stop moving immediately
    public void stop() {
        // Set the motor speeds to zero
        leftDrive.stop();
        rightDrive.stop();
    }

    public void togglePrecisionMode() {
        precision = !precision;
    }

    public void driveContinuousSpeed(double turn, double speed) {
        driveContinuousSpeed(turn, speed, false);
    }

    // Start driving with a given turning coefficient and speed from zero to one
    public void driveContinuousSpeed(double turn, double speed, boolean auto) {
        // If the speed is zero, turn on the spot
        if (speed == 0) {
            rotate(turn);
        }
        // Otherwise, use the regular algorithm
        else {
            WheelSpeeds wheelSpeeds = generalizedCheesyDrive(turn * 0.4, speed);

            driveFiltered(wheelSpeeds, auto);
        }
    }

    // Turn on the spot with the given left wheel speed
    public void rotate(double leftWheelSpeed) {
        WheelSpeeds speeds = new WheelSpeeds(leftWheelSpeed, -leftWheelSpeed);
        driveUnfiltered(speeds);
    }

    private void driveUnfiltered(WheelSpeeds speeds) {
        leftDrive.setConstantSpeed(speeds.leftWheelSpeed);
        rightDrive.setConstantSpeed(speeds.rightWheelSpeed);
    }

    // Drive at the given wheel speeds, applying a low pass filter
    private void driveFiltered(WheelSpeeds speeds, boolean auto) {
        // Run the wheel speeds through corresponding low pass filters
        WheelSpeeds preciseFiltered = preciseFilterSpeeds(speeds);

        if (auto) {
            driveUnfiltered(preciseFiltered);
        } else {
            WheelSpeeds lowPassFilteredSpeeds = lowPassFilter(preciseFiltered);
            driveUnfiltered(lowPassFilteredSpeeds);
        }
    }

    private WheelSpeeds preciseFilterSpeeds(WheelSpeeds speeds) {
        if (this.precision) {
            return new WheelSpeeds(
                    speeds.leftWheelSpeed * 0.5,
                    speeds.rightWheelSpeed * 0.5
            );
        }

        return speeds;
    }

    // A function that takes a turning coefficient from -1 to 1 and a speed and calculates the
    // left and right wheel speeds using a generalized cheesy drive algorithm
    // Credit to Team 254 for the original algorithm
    private WheelSpeeds generalizedCheesyDrive(double turn, double speed) {
        if (speed == 0) {
            return new WheelSpeeds(turn, -turn);
        }
        // Apply a polynomial function to the speed and multiply it by the turning coefficient
        // This adds a non-linearity so that turning is quicker at lower speeds
        // This number will be half of the difference in speed between the two wheels
        // Get the sign of the speed and use the absolute value because the polynomial may give
        // imaginary numbers for negative speed values
        double speedSign = speed > 0 ? 1 : -1;
        // Use the absolute value in the polynomial calculation
        // and multiply the result by the sign
        double wheelSpeedDifference = speedPolynomial(Math.abs(speed)) * turn * speedSign;
        // Add this difference to the overall speed to get the left wheel speed and subtract it
        // from the overall speed to get the right wheel speed
        return new WheelSpeeds(
                speed + wheelSpeedDifference,
                speed - wheelSpeedDifference
        );
    }

    // The polynomial function applied to the speed during turn computation
    private double speedPolynomial(double speed) {
        // Use a simple square root function for the polynomial (an exponent of 0.5)
        // This increases the weight of the difference between the wheel speeds at low speeds
        // because the square root is relatively large in magnitude for values close to zero
        // Multiply the result of the square root by 2 to increase the turning sensitivity
        return Math.sqrt(speed) * 2;
    }

    // A function to run wheel speeds through the corresponding low pass filters
    private WheelSpeeds lowPassFilter(WheelSpeeds speeds) {
        return new WheelSpeeds(
                leftSideLpf.calculate(speeds.leftWheelSpeed),
                rightSideLpf.calculate(speeds.rightWheelSpeed)
        );
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new OperatorDriveCommand());
    }

    @Override
    public void reset() {
    }

    // A wrapper class that contains a speed value for each of the drive base wheels
    private class WheelSpeeds {

        // Speeds for the left and right wheel
        private double leftWheelSpeed;
        private double rightWheelSpeed;

        // Constructor that takes parameters for each of the wheel speeds
        private WheelSpeeds(double leftWheelSpeed, double rightWheelSpeed) {
            // Set the global variables
            this.leftWheelSpeed = leftWheelSpeed;
            this.rightWheelSpeed = rightWheelSpeed;
        }
    }
}
