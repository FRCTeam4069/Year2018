package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.InlineCommandGroup;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.vacuum.SetVacuumSpeedCommand;
import frc.team4069.robot.commands.vacuum.StopVacuumCommand;
import frc.team4069.robot.spline.SplinePath;

public class SwitchHalfScaleCommandGroup extends CommandGroup {

    public SwitchHalfScaleCommandGroup(String gameData){
        System.out.println("REACHED1");
        char switchSide = gameData.charAt(0);
        char scaleSide = gameData.charAt(1);
        if(switchSide == 'R' && scaleSide == 'R'){
            SplinePath path = SplinePath.getSplinePath("splinepathswitchright");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            /*addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(-90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-2.5, 0.5));
            addSequential(new StopVacuumCommand());
            addSequential(new RotateToAngleWithGyroCommand(80, 0.6));
            addParallel(new SetElevatorPositionCommand(-29000));
            addSequential(new RunWithDelayCommand(0, new DriveStraightForDistanceCommand(2.0, 0.5)));*/
        }
        else if(switchSide == 'R' && scaleSide == 'L'){
            SplinePath path = SplinePath.getSplinePath("splinepathswitchright");
            SplinePath path2 = SplinePath.getSplinePath("rrr");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            /*addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(-90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addParallel(new InlineCommandGroup(new RunWithDelayCommand(2500, new StopVacuumCommand()), new SetElevatorPositionCommand(-29000)));
            addSequential(new FollowSplinePathCommand(path2).terminate(0.5));*/
        }
        else if(switchSide == 'L' && scaleSide == 'R'){
            SplinePath path = SplinePath.getSplinePath("splinepathll1");
            SplinePath path2 = SplinePath.getSplinePath("lll");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            /*addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addParallel(new InlineCommandGroup(new RunWithDelayCommand(2500, new StopVacuumCommand()), new SetElevatorPositionCommand(-29000)));
            addSequential(new FollowSplinePathCommand(path2).terminate(0.5));*/
        }
        else{
            SplinePath path = SplinePath.getSplinePath("splinepathll1");
            addParallel(new RunWithDelayCommand(0, new SetElevatorPositionCommand(-12500)));
            addSequential(new FollowSplinePathCommand(path));
            addSequential(new SetVacuumSpeedCommand(0.7));
            addSequential(new DriveStraightForDistanceCommand(-0.3, 0.8));
            addSequential(new StopVacuumCommand());
            /*addParallel(new SetElevatorPositionCommand(0));
            addSequential(new RotateToAngleWithGyroCommand(90, 0.9));
            addSequential(new SetVacuumSpeedCommand(-1));
            addSequential(new DriveStraightForDistanceCommand(1.0, 0.5));
            addSequential(new DriveStraightForDistanceCommand(-2.5, 0.5));
            addSequential(new StopVacuumCommand());
            addSequential(new RotateToAngleWithGyroCommand(-80, 0.6));
            addParallel(new SetElevatorPositionCommand(-29000));
            addSequential(new RunWithDelayCommand(0, new DriveStraightForDistanceCommand(2.0, 0.5)));*/
        }
    }

}
