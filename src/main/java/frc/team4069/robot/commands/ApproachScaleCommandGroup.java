package frc.team4069.robot.commands;

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

public class ApproachScaleCommandGroup extends CommandGroup {
	
	public ApproachScaleCommandGroup(){
		addSequential(new SetElevatorPositionSlowCommand(-29000));
		addSequential(new FollowSplinePathCommand(SplinePath.splinePathTeleopScale, 50, 3.0, 0.7));
	}
	
}