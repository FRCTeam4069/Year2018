package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.ZeroElevatorCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.spline.DoublePoint;
import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.spline.SplinePathSwitchRight;
import frc.team4069.robot.spline.SplinePathSwitchLeft;
import frc.team4069.robot.spline.SplinePathFarSwitch;
import java.util.ArrayList;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // The number of milliseconds after which to stop searching for the game data and choose a
    // reasonable default
    private final int gameDataTimeoutMilliseconds = 250;
	
    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
		/*splinePoints.add(new DoublePoint(0 * scale, 0 * scale));
		splinePoints.add(new DoublePoint(-1.0 * scale, 1.0 * scale));
		splinePoints.add(new DoublePoint(0 * scale, 2.0 * scale));
		splinePoints.add(new DoublePoint(1.0 * scale, 1.0 * scale));
		splinePoints.add(new DoublePoint(0 * scale, 0 * scale));
		splinePoints.add(new DoublePoint(-1.0 * scale, -1.0 * scale));
		splinePoints.add(new DoublePoint(0, -2.0 * scale));
		splinePoints.add(new DoublePoint(1.0 * scale, -1.0 * scale));
		splinePoints.add(new DoublePoint(0 * scale, 0 * scale));*/
        /*addSequential(new StartVacuumCommand());
        addSequential(new ZeroElevatorCommand());
        addSequential(new GrabCubeCommandGroup());
//        if (shouldGoRight()) {
//            addSequential(new DriveTowardTapeCommand(1750));
//        } else {
        addSequential(new DriveStraightForDistanceCommand(0.5, 0.4));
        addSequential(new RotateToAngleWithGyroCommand(-70));
        addSequential(new DriveStraightForDistanceCommand(2.7, 0.7));
        addSequential(new RotateToAngleWithGyroCommand(70));*/
		SplinePath path = new SplinePathSwitchRight();
		addSequential(new FollowSplinePathCommand(path));
        /*addSequential(new DriveTowardTapeCommand(1000));
        addSequential(new StopVacuumCommand());*/
//        }
    }

    // Read the game data and get the direction to drive
    private boolean shouldGoRight() {
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
        return isRight;
    }
}
