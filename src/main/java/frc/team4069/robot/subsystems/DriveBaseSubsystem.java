package frc.team4069.robot.subsystems;

import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonMotor;
import frc.team4069.robot.util.LowPassFilter;

// A class that manages all hardware components of the drive base and provides utility functions
// for instructing it to drive and turn in a variety of ways
public class DriveBaseSubsystem extends SubsystemBase {

    // The lateral distance between the robot's wheels in meters
    public static final double ROBOT_TRACK_WIDTH_METERS = 0.6;

    // A singleton instance of the drive base subsystem
    private static DriveBaseSubsystem instance;

    // The number of meters each wheel travels per rotation
    private final double METERS_PER_ROTATION = 0.2;

    // Left and right drive motors
    private TalonMotor leftDrive;
    private TalonMotor rightDrive;

    private LowPassFilter leftSideLpf;
    private LowPassFilter rightSideLpf;

    // Initialize the drive motors
    private DriveBaseSubsystem() {
        // Initialize the motors with predefined port numbers
        leftDrive = new TalonMotor(IOMapping.LEFT_DRIVE_CAN_BUS, false, 11, 13);
        rightDrive = new TalonMotor(IOMapping.RIGHT_DRIVE_CAN_BUS, false, 18, 20);

        leftSideLpf = new LowPassFilter();
        rightSideLpf = new LowPassFilter();
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
                Math.abs(leftDrive.getDistanceTraveledRotations());
        double rightWheelRotationsTraveled =
                Math.abs(rightDrive.getDistanceTraveledRotations());
        double averageRotationsTraveled =
                (leftWheelRotationsTraveled + rightWheelRotationsTraveled) / 2;
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

    public void quickTurn(double turn) {
        WheelSpeeds wheelSpeeds = generalizedCheesyDrive(turn, 0);
        leftDrive.setConstantSpeed(leftSideLpf.calculate(wheelSpeeds.leftWheelSpeed));
        rightDrive.setConstantSpeed(rightSideLpf.calculate(wheelSpeeds.rightWheelSpeed));
    }

    // Start driving with a given turning coefficient and speed from zero to one
    public void driveContinuousSpeed(double turn, double speed) {
        // Use the cheesy drive algorithm to calculate the necessary speeds
        if(speed < 0) {
            turn = -turn;
        }
        WheelSpeeds wheelSpeeds = generalizedCheesyDrive(turn * 0.4, speed);

        // Set the motor speeds with the calculated values
        leftDrive.setConstantSpeed(leftSideLpf.calculate(wheelSpeeds.leftWheelSpeed));
        rightDrive.setConstantSpeed(rightSideLpf.calculate(wheelSpeeds.rightWheelSpeed));
    }

    // A function that takes a turning coefficient from -1 to 1 and a speed and calculates the
    // left and right wheel speeds using a generalized cheesy drive algorithm
    // Credit to Team 254 for the original algorithm
    private WheelSpeeds generalizedCheesyDrive(double turn, double speed) {
        // First, a special case: if the speed is zero, turn on the spot
        if (speed == 0) {
            // Simply use the turn coefficient and its negative for the wheel speeds, since it is
            // within the range of -1 and 1, and a sharper turn will result in faster rotation
            if(turn == 0) {
                return new WheelSpeeds(0, 0);
            } else {
                return new WheelSpeeds(turn, -turn);
            }
        }
        // Otherwise, we will use the standard algorithm
        else {
            // Apply a polynomial function to the speed and multiply it by the turning coefficient
            // This adds a non-linearity so that turning is quicker at lower speeds
            // This number will be half of the difference in speed between the two wheels
            // Get the sign of the speed and use the absolute value because the polynomial may give
            // imaginary numbers for negative speed values
            double speedSign = speed > 0 ? 1 : -1;
            // Use the absolute value in the polynomial calculation
            // and multiply the result by the sign
            double wheelSpeedDifference = speedPolynomial(Math.abs(speed)) * turn * speedSign;
            //TODO: Get that constant into smart dashboard
            // Add this difference to the overall speed to get the left wheel speed and subtract it
            // from the overall speed to get the right wheel speed
            return new WheelSpeeds(
                    speed + wheelSpeedDifference,
                    speed - wheelSpeedDifference
            );
        }
    }

    // The polynomial function applied to the speed during turn computation
    private double speedPolynomial(double speed) {
        // Use a simple square root function for the polynomial (an exponent of 0.5)
        // This increases the weight of the difference between the wheel speeds at low speeds
        // because the square root is relatively large in magnitude for values close to zero
        // Multiply the result of the square root by 2 to increase the turning sensitivity
        return Math.sqrt(speed) * 2;
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
