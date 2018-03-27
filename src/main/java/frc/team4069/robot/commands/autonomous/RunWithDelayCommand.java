package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.ZeroElevatorCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.autonomous.DriveStraightForDistanceCommand;
import frc.team4069.robot.commands.DelayCommand;
import frc.team4069.robot.commands.CommandBase;
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

public class RunWithDelayCommand extends CommandGroup {
	
    public RunWithDelayCommand(int millis, CommandBase command) {
		addSequential(new DelayCommand(millis));
		addSequential(command);
    }
	
}
