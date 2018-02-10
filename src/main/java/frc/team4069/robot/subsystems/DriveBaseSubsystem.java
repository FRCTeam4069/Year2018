package frc.team4069.robot.subsystems;

import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;
import frc.team4069.robot.util.LowPassFilter;

// A class that manages all hardware components of the drive base and provides utility functions
// for instructing it to drive and turn in a variety of ways
public class DriveBaseSubsystem extends SubsystemBase {

    // The lateral distance between the robot's wheels in meters
    public static final double ROBOT_TRACK_WIDTH_METERS = 0.6;
    // A singleton instance of the drive base subsystem
    private static DriveBaseSubsystem instance;
    // The factor by which the reciprocal of the error is multiplied to get a speed multiplier
    private final double CORRECTION_SCALE = 1.5;
    // The number of meters each wheel travels per motor rotation
    private final double METERS_PER_ROTATION = 0.61;
    // The number of past distances traveled to retain
    private final int DISTANCES_TRAVELED_HISTORY = 10;

    // Left and right drive motors
    private TalonSRXMotor leftDrive;
    private TalonSRXMotor rightDrive;
    // Low pass filters that smooth steering
    private LowPassFilter leftSideLpf;
    private LowPassFilter rightSideLpf;
    // An array of past distances traveled in rotations by each of the wheels
    private double[] leftWheelDistancesTraveled;
    private double[] rightWheelDistancesTraveled;

    // Initialize the drive motors
    private DriveBaseSubsystem() {
        // Initialize the motors with predefined port numbers
        leftDrive = new TalonSRXMotor(IOMapping.LEFT_DRIVE_CAN_BUS, 256, false,  11, 13);
        rightDrive = new TalonSRXMotor(IOMapping.RIGHT_DRIVE_CAN_BUS, 256, false, 18, 20);
        // Initialize the low pass filters with a time period of 200 milliseconds
        leftSideLpf = new LowPassFilter(200);
        rightSideLpf = new LowPassFilter(200);
        // Initialize the arrays of distances traveled with zeroes
        leftWheelDistancesTraveled = new double[DISTANCES_TRAVELED_HISTORY];
        rightWheelDistancesTraveled = new double[DISTANCES_TRAVELED_HISTORY];
        for (int i = 0; i < DISTANCES_TRAVELED_HISTORY; i++) {
            leftWheelDistancesTraveled[i] = 0;
            rightWheelDistancesTraveled[i] = 0;
        }
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
        // Wheel speeds that will be set using the drive algorithms
        WheelSpeeds wheelSpeeds = generalizedCheesyDrive(turn * 0.4, speed);
        // A special case: if the speed is zero, turn on the spot
        // Correct the wheel speeds based on positional errors
        WheelSpeeds correctedWheelSpeeds = correctSteering(wheelSpeeds);
        // Run the wheel speeds through corresponding low pass filters
        WheelSpeeds lowPassFilteredSpeeds = lowPassFilter(wheelSpeeds);
        // Set the motor speeds with the calculated values

        leftDrive.setConstantSpeed(lowPassFilteredSpeeds.leftWheelSpeed);
        rightDrive.setConstantSpeed(lowPassFilteredSpeeds.rightWheelSpeed);
    }

    // A function that takes a turning coefficient from -1 to 1 and a speed and calculates the
    // left and right wheel speeds using a generalized cheesy drive algorithm
    // Credit to Team 254 for the original algorithm
    private WheelSpeeds generalizedCheesyDrive(double turn, double speed) {
        if(speed == 0) {
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

    // Modify a set of wheel speeds to correct for errors that have accumulated due to friction
    private WheelSpeeds correctSteering(WheelSpeeds rawSpeeds) {
        // Shift all of the elements in the history of distances down one place to the end
        for (int i = 1; i < DISTANCES_TRAVELED_HISTORY; i++) {
            leftWheelDistancesTraveled[i] = leftWheelDistancesTraveled[i - 1];
            rightWheelDistancesTraveled[i] = rightWheelDistancesTraveled[i - 1];
        }
        // Add the current distances traveled by each of the wheels and set the first element of
        // each of the lists accordingly
        leftWheelDistancesTraveled[0] = leftDrive.getDistanceTraveledRotations();
        rightWheelDistancesTraveled[0] = rightDrive.getDistanceTraveledRotations();
        // Get the number of rotations traveled by each wheel since the beginning of the recorded
        // history of distances traveled, and calculate the average
        double leftWheelRotationsTraveled = leftDrive.getDistanceTraveledRotations()
                - leftWheelDistancesTraveled[DISTANCES_TRAVELED_HISTORY - 1];
        double rightWheelRotationsTraveled = rightDrive.getDistanceTraveledRotations()
                - rightWheelDistancesTraveled[DISTANCES_TRAVELED_HISTORY - 1];
        double averageRotationsTraveled =
                (leftWheelRotationsTraveled + rightWheelRotationsTraveled) / 2;
        // Calculate the ratios of the distance traveled by each wheel versus the average
        double leftWheelDistanceRatio = leftWheelRotationsTraveled / averageRotationsTraveled;
        double rightWheelDistanceRatio = rightWheelRotationsTraveled / averageRotationsTraveled;
        // Calculate the average speed of the two wheels
        double averageWheelSpeed = (rawSpeeds.leftWheelSpeed + rawSpeeds.rightWheelSpeed) / 2;
        // Calculate the ratios of the speed demanded of each wheel versus the average
        double leftWheelSpeedRatio = rawSpeeds.leftWheelSpeed / averageWheelSpeed;
        double rightWheelSpeedRatio = rawSpeeds.rightWheelSpeed / averageWheelSpeed;
        // Calculate the errors of the distance ratios versus the expected speed ratios
        double leftWheelError = leftWheelDistanceRatio / leftWheelSpeedRatio;
        double rightWheelError = rightWheelDistanceRatio / rightWheelSpeedRatio;
        // Multiply the reciprocals of the errors by a scaling factor to get correction factors
        double leftWheelCorrection = (1 / leftWheelError) * CORRECTION_SCALE;
        double rightWheelCorrection = (1 / rightWheelError) * CORRECTION_SCALE;
        // Return the wheel speeds multiplied by the corresponding correction factors
        return new WheelSpeeds(
                rawSpeeds.leftWheelSpeed * leftWheelCorrection,
                rawSpeeds.rightWheelSpeed * rightWheelCorrection
        );
    }

    // A function to run wheel speeds through the corresponding low pass filters
    private WheelSpeeds lowPassFilter(WheelSpeeds speeds) {
        return new WheelSpeeds(
                leftSideLpf.calculate(speeds.leftWheelSpeed),
                rightSideLpf.calculate(speeds.rightWheelSpeed)
        );
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
