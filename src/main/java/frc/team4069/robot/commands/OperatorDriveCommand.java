package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.subsystems.ElevatorSubsystem;

// The main command for operator control of the drive base
public class OperatorDriveCommand extends CommandBase {

    // The distance that the current quick turn should travel (0 means quick turn is not being used)
    private double quickTurnDistanceMeters = 0;

    // Constructor, used to claim subsystems
    public OperatorDriveCommand() {
        // Claim exclusive use of the drive base
        requires(driveBase);
    }

    // Called to initialize the drive base
    protected void initialize() {
        // It should start out idle
        driveBase.stop();
    }

    // Called frequently while this command is being run
    protected void execute() {
        // Set drive base speeds using the joystick inputs
        // The turning coefficient should be equal to the steering axis
        double turningCoefficient = Input.getSteeringAxis();
        // Use the negative of the joystick's speed axis as the speed of the drive base
        double speed = Input.getDriveSpeed();

        double elevatorPosition = Math.abs(elevator.getPosition()) - Math.abs(
                ElevatorSubsystem.MAX_POSITION_TICKS);

		//System.out.println("Left wheel encoder: " + driveBase.getDistanceTraveledMetersLeftWheel());
		//System.out.println("Right wheel encoder: " + driveBase.getDistanceTraveledMetersRightWheel());
        // Dampen the drive base input if the elevator is raised ~halfway
        // TODO: Check the values here
        if (Math.abs(elevatorPosition) <= 16000 && elevatorPosition > 0) {
            speed *= 0.5;
            turningCoefficient *= 0.5;
        }

        // If quick turn is not currently being used
        if (quickTurnDistanceMeters == 0) {
            // Get output from the directional pad
            int directionalPadAngle = Input.getDriveDirectionalPadDegrees();
            // If the directional pad is being used (the output is not -1) and the up direction is not
            // being pressed (an output of 0)
            if (directionalPadAngle != -1 && directionalPadAngle != 0) {
                // If the angle is greater than 180
                if (directionalPadAngle > 180) {
                    // Subtract 360 so that the robot rotates in the opposite direction
                    directionalPadAngle -= 360;
                }

                // Multiply the angle in rotations by the horizontal distance between the wheels
                // wheels in meters multiplied by pi, to get the length of the arc that the wheels should travel
                quickTurnDistanceMeters = (((double) directionalPadAngle) / 360)
                        * DriveBaseSubsystem.ROBOT_TRACK_WIDTH_METERS
                        * Math.PI;
                // Get the sign of the angle in order to calculate the direction to turn the wheels
                double turnDirection = Math.signum(quickTurnDistanceMeters);
                // Start turning at full speed in the direction of the sign
                driveBase.rotate(turnDirection);
            }
        }

        // If quick turn is currently running
        else {
            // If the distance travelled so far is greater than or equal to the quick turn distance
            if (driveBase.getDistanceTraveledMeters() >= Math.abs(quickTurnDistanceMeters)) {
                // Stop turning
                driveBase.stop();
                // Disable quick turn
                quickTurnDistanceMeters = 0;
            }
        }

        // If the driver is using the regular controls or if quick turn is disabled
        if (Math.abs(turningCoefficient) >= 0.2
                || Math.abs(speed) >= 0.2
                || quickTurnDistanceMeters == 0) {
            // Disable quick turn
            quickTurnDistanceMeters = 0;
            // Set the speed of the robot
            driveBase.driveContinuousSpeed(turningCoefficient, speed);
        }

    }

    // Called to check whether this command has completed
    protected boolean isFinished() {
        // Always return false because this command should never finish
        return false;
    }
}
