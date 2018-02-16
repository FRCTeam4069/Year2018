package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveSquareCommandGroup extends CommandGroup {

    public DriveSquareCommandGroup() {
        addSequential(new DriveStraightForDistanceCommand(2));
        addSequential(new RotateToAngleWithGyroCommand(90));
        addSequential(new DriveStraightForDistanceCommand(2));
        addSequential(new RotateToAngleWithGyroCommand(180));
        addSequential(new DriveStraightForDistanceCommand(2));
        addSequential(new RotateToAngleWithGyroCommand(270));
        addSequential(new DriveStraightForDistanceCommand(2));
        addSequential(new RotateToAngleWithGyroCommand(0));
    }

}
