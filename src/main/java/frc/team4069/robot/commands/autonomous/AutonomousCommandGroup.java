package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4069.robot.commands.elevator.ZeroElevatorCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // The number of milliseconds after which to stop searching for the game data and choose a
    // reasonable default
    private final int gameDataTimeoutMilliseconds = 250;
    // The robot's starting position for autonomous mode
    // 0 is left, 1 is center, and 2 is right
    private int startingPosition = 2;

    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
        addSequential(new StartVacuumCommand());
        addSequential(new ZeroElevatorCommand());
        addSequential(new WaitCommand(1));
        addSequential(new GrabCubeCommand());
        addSequential(new RotateToAngleWithGyroCommand(-60));
        addSequential(new DriveStraightForDistanceCommand(9));
        addSequential(new RotateToAngleWithGyroCommand(60));
        addSequential(new DriveTowardTapeCommand());
        addSequential(new StopVacuumCommand());
        addSequential(new WaitCommand(2));
        addSequential(new StartVacuumCommand());
        addSequential(new DriveStraightForDistanceCommand(-1));
        addSequential(new RotateToAngleWithGyroCommand(90));
        addSequential(new DriveStraightForDistanceCommand(1));
        addSequential(new GrabCubeCommand());
        addSequential(new DriveStraightForDistanceCommand(-1));
        addSequential(new RotateToAngleWithGyroCommand(-90));
        addSequential(new DriveStraightForDistanceCommand(1));
        addSequential(new StopVacuumCommand());
    }

    // Read the game data and get the direction to drive
    private int getTurningParametersIndex() {
        String gameData = "";
        long startingTime = System.currentTimeMillis();
        // Assume that the left switch is the one to go to
        boolean isRight = false;
        // Loop until the timeout period is up
        while (System.currentTimeMillis() - startingTime < gameDataTimeoutMilliseconds) {
            // Get the game data as a string
            gameData = DriverStation.getInstance().getGameSpecificMessage();
            // If the length of the game data string is 3 as it should be
            if (gameData.length() == 3) {
                // Check if the first character is left or right
                isRight = gameData.charAt(0) == 'R';
                break;
            }
        }
        // Get the index of the turning parameters by taking the starting position and adding 3 if
        // the right switch is being used
        return startingPosition + (isRight ? 3 : 0);
    }
}
