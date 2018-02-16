package frc.team4069.robot.commands.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.autonomous.RotateToAngleWithGyroCommand;

public class HookArmCommandGroup extends CommandGroup {
    public HookArmCommandGroup() {
//        addSequential(new DeployArmCommand());
//        addSequential(new DriveStraightForDistanceCommand(1.5));
//        addSequential(new DriveStraightForDistanceCommand(-1.5));
//        addSequential(new RetractArmCommand());
//        addSequential(new DriveStraightForDistanceCommand(1.5));
        addSequential(new RotateToAngleWithGyroCommand(30));
    }
}
