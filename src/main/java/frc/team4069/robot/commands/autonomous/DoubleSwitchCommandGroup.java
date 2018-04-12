package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.vacuum.SetVacuumSpeedCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.spline.SplinePath;

public class DoubleSwitchCommandGroup extends CommandGroup {

    public DoubleSwitchCommandGroup(String gameData){
        char switchSide = gameData.charAt(0);
        if(switchSide == 'R'){
            SplinePath path = SplinePath.getSplinePath("splinepathswitchright");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(-90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(1.3, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-1.3, 0.5));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(-12500));
            addSequential(new RotateToAngleWithGyroCommand(90, 0.9));
            addSequential(new DriveStraightForDistanceCommand(0.25, 0.8));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.7, 0.8));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(-45, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(0.8, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-0.8, 0.5));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(-12500));
            addSequential(new RotateToAngleWithGyroCommand(45, 0.9));
            addSequential(new DriveStraightForDistanceCommand(0.6, 0.8));
            addSequential(new SetVacuumSpeedCommand(0.7));
        }
        else{
            SplinePath path = SplinePath.getSplinePath("splinepathll1");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(1.3, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-1.3, 0.5));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(-12500));
            addSequential(new RotateToAngleWithGyroCommand(-90, 0.9));
            addSequential(new DriveStraightForDistanceCommand(0.25, 0.8));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.7, 0.8));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(45, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(0.8, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-0.8, 0.5));
            addSequential(new StopVacuumCommand());
            addParallel(new SetElevatorPositionCommand(-12500));
            addSequential(new RotateToAngleWithGyroCommand(-45, 0.9));
            addSequential(new DriveStraightForDistanceCommand(0.6, 0.8));
            addSequential(new SetVacuumSpeedCommand(0.7));
        }
    }

}
