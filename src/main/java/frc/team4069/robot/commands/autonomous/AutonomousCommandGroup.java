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
import java.util.ArrayList;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // The number of milliseconds after which to stop searching for the game data and choose a
    // reasonable default
    private final int gameDataTimeoutMilliseconds = 250;
	
	public static String gameInfo;
	
    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
		String gameData = getGameData();
		gameInfo = gameData;
		char switchSide = gameData.charAt(0);
		char scaleSide = gameData.charAt(1);
		if(switchSide == 'R' && scaleSide == 'R'){
			SplinePath path = SplinePath.splinePathSwitchRight;
			addParallel(new RunWithDelayCommand(2500, new SetElevatorPositionCommand(-10000)));
			addSequential(new FollowSplinePathCommand(path));
			addSequential(new StartVacuumCommand());
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.8));
			addParallel(new SetElevatorPositionCommand(0));
			addSequential(new RotateToAngleWithGyroCommand(-86, 0.9));
			addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
			addSequential(new DriveStraightForDistanceCommand(-2.5, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(80, 0.6));
			addParallel(new SetElevatorPositionSlowCommand(-29000));
			addSequential(new RunWithDelayCommand(0, new DriveStraightForDistanceCommand(4.75, 0.6)));
			addSequential(new StopVacuumCommand());
		}
		else if(switchSide == 'R' && scaleSide == 'L'){
			SplinePath path = SplinePath.splinePathRL1;
			SplinePath path2 = SplinePath.splinePathRL2;
			addSequential(new FollowSplinePathCommand(path, 10, 3.0, 0.8));
			addSequential(new RotateToAngleWithGyroCommand(-90));
			addParallel(new DriveStraightForDistanceCommand(0.4, 0.5));
			addSequential(new SetElevatorPositionCommand(-13000));
			addSequential(new StartVacuumCommand());
			addParallel(new DriveStraightForDistanceCommand(-0.4, 0.5));
			addSequential(new SetElevatorPositionCommand(0));
			addSequential(new DriveStraightForDistanceCommand(0.2, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(90, 0.7));
			addParallel(new RunWithDelayCommand(0, new FollowSplinePathCommand(path2)));
			addSequential(new SetElevatorPositionSlowCommand(-29000, 0.7));
			addSequential(new StopVacuumCommand());
		}
		else if(switchSide == 'L' && scaleSide == 'R'){
			SplinePath path = SplinePath.splinePathLR1;
			SplinePath path2 = SplinePath.splinePathLR2;
			addSequential(new FollowSplinePathCommand(path, 10, 3.0, 0.8));
			addSequential(new RotateToAngleWithGyroCommand(90));
			addParallel(new DriveStraightForDistanceCommand(0.4, 0.5));
			addSequential(new SetElevatorPositionCommand(-13000));
			addSequential(new StartVacuumCommand());
			addParallel(new DriveStraightForDistanceCommand(-0.4, 0.5));
			addSequential(new SetElevatorPositionCommand(0));
			addSequential(new DriveStraightForDistanceCommand(0.2, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(-90, 0.7));
			addParallel(new RunWithDelayCommand(0, new FollowSplinePathCommand(path2)));
			addSequential(new SetElevatorPositionSlowCommand(-29000, 0.7));
			addSequential(new StopVacuumCommand());
		}
		else{
			SplinePath path = SplinePath.splinePathLL1;
			addParallel(new RunWithDelayCommand(2500, new SetElevatorPositionCommand(-10000)));
			addSequential(new FollowSplinePathCommand(path));
			addSequential(new StartVacuumCommand());
			addSequential(new DriveStraightForDistanceCommand(-0.35, 0.8));
			addParallel(new SetElevatorPositionCommand(0));
			addSequential(new RotateToAngleWithGyroCommand(86, 0.9));
			addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
			addSequential(new DriveStraightForDistanceCommand(-2.5, 0.5));
			addSequential(new RotateToAngleWithGyroCommand(-80, 0.6));
			addParallel(new SetElevatorPositionSlowCommand(-29000));
			addSequential(new RunWithDelayCommand(0, new DriveStraightForDistanceCommand(4.75, 0.6)));
			addSequential(new StopVacuumCommand());
		}
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
	
}
