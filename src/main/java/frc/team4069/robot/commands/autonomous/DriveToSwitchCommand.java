package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team4069.robot.commands.CommandBase;

// Drive from the starting position to the switch in autonomous mode
class DriveToSwitchCommand extends CommandBase {

    // The robot's starting position for autonomous mode
    private StartingPosition startingPosition = StartingPosition.RIGHT;

    // Constructor, used to claim subsystems
    DriveToSwitchCommand() {
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
        // Calculate the turn using the game data
        double turn = getTurnFromGameData();
        // Drive at three quarters of full speed using the calculated turn value
        driveBase.driveContinuousSpeed(turn, 0.25);
    }

    // Read the game data and get the direction to drive
    private double getTurnFromGameData() {
        // Get the game data as a string
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        // Assume that the left switch is the one to go to
        boolean isRight = false;
        // If the length of the game data string is 3 as it should be
        if (gameData.length() == 3) {
            // Check if the first character is left or right
            isRight = gameData.charAt(0) == 'R';
        }

        // Assuming the switch is on the left, switch on the starting position and get the relevant
        // turning angle
        double turn = 0;
        switch (startingPosition) {
            case LEFT:
                turn = -0.05;
            case CENTER:
                turn = 0.05;
            case RIGHT:
                turn = 0.15;
        }
        // If it is on the right, subtract 0.1 so that center is -0.05
        if (isRight) {
            turn -= 0.1;
        }
        // Return the modified turn value
        return turn;
    }

    // Called to check if the command has completed
    protected boolean isFinished() {
        // Check if the drive base has gone 5 meters
        return driveBase.getDistanceTraveledMeters() >= 1;
    }

    // Used to stop the drive base when the command finishes
    protected void end() {
        driveBase.stop();
    }

    // An enum containing the possible starting positions of the robot
    private enum StartingPosition {
        LEFT,
        CENTER,
        RIGHT
    }
}
