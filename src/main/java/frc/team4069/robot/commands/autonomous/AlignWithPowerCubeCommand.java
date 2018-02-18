package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// With the front camera positioned directly above a power cube, move the robot to align with it
public class AlignWithPowerCubeCommand extends CommandBase {

    // If the vertical position of the cube is below this threshold, consider the motion complete
    private final int verticalPositionThreshold = 10;

    // Constructor, used to claim subsystems
    public AlignWithPowerCubeCommand() {
        // Claim exclusive use of the drive base
        requires(driveBase);
    }

    // Called frequently while this command is being run
    protected void execute() {
        // Scale the power cube's horizontal position into the range of -1 to 1
        int horizontalDistanceFromImageCenterToEdge = getArmCameraImageWidth() / 2;
        int positionRelativeToImageCenter =
                getPowerCubeXPos() - horizontalDistanceFromImageCenterToEdge;
        double horizontalPositionNegative1To1 = (double) positionRelativeToImageCenter
                / (double) horizontalDistanceFromImageCenterToEdge;
        // Scale the vertical position into the range of 0 to 1
        double verticalPosition0To1 =
                (double) getPowerCubeYPos() / (double) getArmCameraImageHeight();
        // Drive the robot, using the scaled horizontal position as a turn value and the vertical
        // position as a speed
        driveBase.driveContinuousSpeed(horizontalPositionNegative1To1, verticalPosition0To1);
    }

    // Called to check whether this command has completed
    protected boolean isFinished() {
        // Check if the vertical position of the power cube is below the predefined threshold
        return getPowerCubeYPos() < verticalPositionThreshold;
    }

}