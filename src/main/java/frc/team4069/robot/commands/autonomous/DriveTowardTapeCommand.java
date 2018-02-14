package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.vision.ThreadVisionProcessor.ColourRegions;

// Drive straight until the tape is found, and then follow it
class DriveTowardTapeCommand extends CommandBase {

    // Scaling factor for the horizontal error of the tape off the center of the screen
    // It is multiplied by a value from 1 to -1
    private final double errorScalingFactor = 0.5;

    // Distance in centimeters ahead at which the command stops
    private final double stopDistanceCentimeters = 5;

    private final double driveSpeed = 0.2;

    DriveTowardTapeCommand() {
        requires(driveBase);
    }

    @Override
    protected void execute() {
        ColourRegions colourRegions = getColourRegions();
        double turn;
        // If the tape is found
        if (colourRegions.mTargetVisible == 1) {
            // Get the horizontal error of the target off the center of the screen
            double horizontalError = colourRegions.mXCenter - 160;
            // Use the scaled error as the turn value
            turn = (horizontalError / 160) * errorScalingFactor;
            System.out.println(turn);
        } else {
            // Otherwise, drive straight
            turn = 0;
            System.out.println("Straight");
        }
        driveBase.driveContinuousSpeed(turn, driveSpeed);
    }

    @Override
    protected boolean isFinished() {
        return false; // getDistanceAheadCentimeters() <= stopDistanceCentimeters;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
