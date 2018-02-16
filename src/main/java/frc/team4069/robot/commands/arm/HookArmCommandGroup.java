package frc.team4069.robot.commands.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.autonomous.DriveStraightForDistanceCommand;

public class HookArmCommandGroup extends CommandGroup {
    public HookArmCommandGroup() {
//        addSequential(new DeployArmCommand());
        addSequential(new DriveStraightForDistanceCommand(0.3));
//        addSequential(new DriveStraightForDistanceCommand(-0.1));
//        addSequential(new RetractArmCommand());
//        addSequential(new DriveStraightForDistanceCommand(0.1));
//        addSequential(new RotateToAngleWithGyroCommand(30));
    }
}
