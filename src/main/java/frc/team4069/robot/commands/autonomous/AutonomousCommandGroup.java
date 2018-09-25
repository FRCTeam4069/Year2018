package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.Robot;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionSlowCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.commands.vacuum.SetVacuumSpeedCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.autonomous.DriveStraightForDistanceCommand;
import frc.team4069.robot.commands.DelayCommand;
import frc.team4069.robot.commands.InlineCommandGroup;
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
		if(Robot.autoMode == AutoMode.SWITCH_SCALE) {
            addSequential(new SwitchScaleCommandGroup(gameData));
        }
        else if(Robot.autoMode == AutoMode.SWITCH_HALF_SCALE){
		    addSequential(new SwitchHalfScaleCommandGroup(gameData));
        }
        else if(Robot.autoMode == AutoMode.DOUBLE_SCALE_RIGHT){
		    addSequential(new DoubleScaleCommandGroup(gameData, true));
        }
        else if(Robot.autoMode == AutoMode.DOUBLE_SCALE_LEFT){
		    addSequential(new DoubleScaleCommandGroup(gameData, false));
        }
        else if(Robot.autoMode == AutoMode.DOUBLE_SCALE_RIGHT_STRAIGHT_ONLY){
		    addSequential(new DoubleScaleStraightOnlyCommandGroup(gameData, true));
        }
        else if(Robot.autoMode == AutoMode.DOUBLE_SCALE_LEFT_STRAIGHT_ONLY){
		    addSequential(new DoubleScaleStraightOnlyCommandGroup(gameData, false));
        }
        else if(Robot.autoMode == AutoMode.DOUBLE_SWITCH){
		    addSequential(new DoubleSwitchCommandGroup(gameData));
        }
        else if(Robot.autoMode == AutoMode.DRIVE_STRAIGHT) {
            addSequential(new DriveStraightDelayCommandGroup(11000, 4));
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
