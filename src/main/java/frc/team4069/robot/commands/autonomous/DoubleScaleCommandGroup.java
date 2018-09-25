package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.DelayCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.vacuum.SetVacuumSpeedCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.spline.SplinePath;

public class DoubleScaleCommandGroup extends CommandGroup {

    public DoubleScaleCommandGroup(String gameData, boolean rightSide){
        char scaleSide = gameData.charAt(1);
        if(rightSide) {
            if (scaleSide == 'R') {
                SplinePath path = SplinePath.getSplinePath("doublescaleright");
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new FollowSplinePathCommand(path));
                addSequential(new SetVacuumSpeedCommand(0.6));
                addSequential(new DriveStraightForDistanceCommand(-0.964, 0.5));
                addSequential(new DelayCommand(500));
                addParallel(new RunWithDelayCommand(500, new SetElevatorPositionCommand(0).setDownSpeed(0.7)));
                addSequential(new RotateToAngleWithGyroCommand(-161).setCounterThreshold(20));
                addSequential(new SetVacuumSpeedCommand(-1));
                addSequential(new DriveStraightForDistanceCommand(0.5, 0.5));
                addSequential(new DriveStraightForDistanceCommand(-0.5, 0.5));
                addSequential(new StopVacuumCommand());
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new RotateToAngleWithGyroCommand(161).setCounterThreshold(20));
                addSequential(new DriveStraightForDistanceCommand(0.964, 0.5));
                addParallel(new RunWithDelayCommand(1000, new DriveStraightForDistanceCommand(-0.5, 0.5)));
                addSequential(new SetVacuumSpeedCommand(0.5));
            } else if (scaleSide == 'L') {
                SplinePath path = SplinePath.getSplinePath("doublescaleleft");
                addParallel(new SetElevatorPositionCommand(-29000));
                addParallel(new RunWithDelayCommand(4000, new SetElevatorPositionCommand(-29000)));
                addSequential(new FollowSplinePathCommand(path));
                addSequential(new SetVacuumSpeedCommand(0.7));
                addSequential(new DriveStraightForDistanceCommand(-0.65, 0.5));
                addSequential(new DelayCommand(500));
                addParallel(new RunWithDelayCommand(500, new SetElevatorPositionCommand(0).setDownSpeed(0.7)));
                addSequential(new RotateToAngleWithGyroCommand(170));
                addSequential(new SetVacuumSpeedCommand(-1));
                addSequential(new DriveStraightForDistanceCommand(0.5, 0.5));
                addSequential(new DriveStraightForDistanceCommand(-0.5, 0.5));
                addSequential(new StopVacuumCommand());
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new RotateToAngleWithGyroCommand(-170));
                addSequential(new DriveStraightForDistanceCommand(0.65, 0.5));
                addParallel(new RunWithDelayCommand(1000, new DriveStraightForDistanceCommand(-0.5, 0.5)));
                addSequential(new SetVacuumSpeedCommand(0.7));
            }
        }
        else{
            if (scaleSide == 'R') {
                SplinePath path = SplinePath.getSplinePath("doublescaleleftmirror");
                addParallel(new SetElevatorPositionCommand(-29000));
                addParallel(new RunWithDelayCommand(4000, new SetElevatorPositionCommand(-29000)));
                addSequential(new FollowSplinePathCommand(path));
                addSequential(new SetVacuumSpeedCommand(0.7));
                addSequential(new DriveStraightForDistanceCommand(-0.65, 0.5));
                addSequential(new DelayCommand(500));
                addParallel(new RunWithDelayCommand(500, new SetElevatorPositionCommand(0).setDownSpeed(0.7)));
                addSequential(new RotateToAngleWithGyroCommand(-170));
                addSequential(new SetVacuumSpeedCommand(-1));
                addSequential(new DriveStraightForDistanceCommand(0.5, 0.5));
                addSequential(new DriveStraightForDistanceCommand(-0.5, 0.5));
                addSequential(new StopVacuumCommand());
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new RotateToAngleWithGyroCommand(170));
                addSequential(new DriveStraightForDistanceCommand(0.65, 0.5));
                addParallel(new RunWithDelayCommand(1000, new DriveStraightForDistanceCommand(-0.5, 0.5)));
                addSequential(new SetVacuumSpeedCommand(0.7));
            }
            else if (scaleSide == 'L') {
                SplinePath path = SplinePath.getSplinePath("doublescalerightmirror");
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new FollowSplinePathCommand(path));
                addSequential(new SetVacuumSpeedCommand(0.6));
                addSequential(new DriveStraightForDistanceCommand(-0.964, 0.5));
                addSequential(new DelayCommand(500));
                addParallel(new RunWithDelayCommand(500, new SetElevatorPositionCommand(0).setDownSpeed(0.7)));
                addSequential(new RotateToAngleWithGyroCommand(161).setCounterThreshold(20));
                addSequential(new SetVacuumSpeedCommand(-1));
                addSequential(new DriveStraightForDistanceCommand(0.5, 0.5));
                addSequential(new DriveStraightForDistanceCommand(-0.5, 0.5));
                addSequential(new StopVacuumCommand());
                addParallel(new SetElevatorPositionCommand(-29000));
                addSequential(new RotateToAngleWithGyroCommand(-161).setCounterThreshold(20));
                addSequential(new DriveStraightForDistanceCommand(0.964, 0.5));
                addParallel(new RunWithDelayCommand(1000, new DriveStraightForDistanceCommand(-0.5, 0.5)));
                addSequential(new SetVacuumSpeedCommand(0.4));
            }
        }
    }

}
