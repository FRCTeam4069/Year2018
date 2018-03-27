package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.ZeroElevatorCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionSlowCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.autonomous.DriveStraightForDistanceCommand;
import frc.team4069.robot.commands.DelayCommand;
import frc.team4069.robot.commands.autonomous.RunWithDelayCommand;
import frc.team4069.robot.spline.DoublePoint;
import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.spline.SplinePathSwitchRight;
import frc.team4069.robot.spline.SplinePathSwitchLeft;
import frc.team4069.robot.spline.SplinePathSwitchLeftOld;
import frc.team4069.robot.spline.SplinePathCubeRight;
import frc.team4069.robot.spline.SplinePathCubeLeft;
import frc.team4069.robot.spline.SplinePathScaleRight;
import frc.team4069.robot.spline.SplinePathScaleLeft;
import frc.team4069.robot.spline.SplinePathCircle;
import java.util.ArrayList;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // The number of milliseconds after which to stop searching for the game data and choose a
    // reasonable default
    private final int gameDataTimeoutMilliseconds = 250;
	
    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
		/*SplinePath path = new SplinePathSwitchLeftOld();
		addSequential(new FollowSplinePathCommand(path, true));
		addSequential(new SetElevatorPositionCommand(-10000));*/
		/*SplinePath path = new SplinePathSwitchRight();
		SplinePath path2 = new SplinePathCubeRight();
		SplinePath path3 = new SplinePathScaleRight();
		addSequential(new FollowSplinePathCommand(path, true));
		addSequential(new SetElevatorPositionCommand(-10000));
		addSequential(new StartVacuumCommand());
		addParallel(new DriveStraightForDistanceCommand(-2.471, 0.5));
		addSequential(new RunWithDelayCommand(500, new SetElevatorPositionCommand(0)));
		addSequential(new FollowSplinePathCommand(path2, true));
		addSequential(new DriveStraightForDistanceCommand(-1.513, 0.5));
		addParallel(new FollowSplinePathCommand(path3, true));
		addSequential(new RunWithDelayCommand(3500, new SetElevatorPositionCommand(-10000)));*/
		String gameData = getGameData();
		char switchSide = gameData.charAt(0);
		char scaleSide = gameData.charAt(1);
		/*if(switchSide == 'R' && scaleSide == 'R'){
			SplinePath path = new SplinePathSwitchRight();
			addSequential(new FollowSplinePathCommand(path, true));
			addSequential(new SetElevatorPositionCommand(-10000));
			addSequential(new StartVacuumCommand());
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.5));
			addParallel(new SetElevatorPositionCommand(0));
			addSequential(new RotateToAngleWithGyroCommand(-86));
			addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
			addSequential(new DriveStraightForDistanceCommand(-2.5, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(80));
			addParallel(new SetElevatorPositionSlowCommand(-29000));
			addSequential(new RunWithDelayCommand(1000, new DriveStraightForDistanceCommand(4.75, 0.5)));
			addSequential(new StopVacuumCommand());
		}
		else if(switchSide == 'R' && scaleSide == 'L'){
			SplinePath path = new SplinePathRL1();
			SplinePath path2 = new SplinePathRL2();
			addSequential(new FollowSplinePathCommand(path));
			addSequential(new SetElevatorPositionCommand(-10000));
			addSequential(new StartVacuumCommand());
			addParallel(new SetElevatorPositionCommand(0));
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.5));
			addSequential(new DriveStraightForDistanceCommand(0.1, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(90));
			addParallel(new SetElevatorPositionSlowCommand(-29000));
			addSequential(new FollowSplinePathCommand(path2));
			addSequential(new StopVacuumCommand());
		}
		else if(switchSide == 'L' && scaleSide == 'R'){
			SplinePath path = new SplinePathSwitchLeft();
			addSequential(new FollowSplinePathCommand(path));
			addSequential(new SetElevatorPositionCommand(-10000));
			addSequential(new StartVacuumCommand());
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(86));
			addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(30));
			addParallel(new RunWithDelayCommand(3000, new SetElevatorPositionSlowCommand(-29000)));
			addSequential(new FollowSplinePathCommand(path2));
			addSequential(new StopVacuumCommand());
		}
		else{
			SplinePath path = new SplinePathLL1();
			SplinePath path2 = new SplinePathLL2();
			addSequential(new FollowSplinePathCommand(path));
			addSequential(new SetElevatorPositionCommand(-10000));
			addSequential(new StartVacuumCommand());
			addParallel(new SetElevatorPositionCommand(0));
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.5));
			addSequential(new DriveStraightForDistanceCommand(0.1, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(90));
			addParallel(new SetElevatorPositionSlowCommand(-29000));
			addSequential(new FollowSplinePathCommand(path2));
			addSequential(new StopVacuumCommand());
		}*/
    }

	private String getGameData(){
		String gameData = "";
        long startingTime = System.currentTimeMillis();
        // Loop until the timeout period is up
        while (System.currentTimeMillis() - startingTime < gameDataTimeoutMilliseconds) {
            // Get the game data as a string
            gameData = DriverStation.getInstance().getGameSpecificMessage();
            // If the length of the game data string is 3 as it should be
            if (gameData.length() == 3) {
                break;
            }
        }
		if(gameData.length() == 0){
			return "RRR";
		}
        // Get the index of the turning parameters by taking the starting position and adding 3 if
        // the right switch is being used
        return gameData;
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
