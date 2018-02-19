package frc.team4069.robot.commands.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4069.robot.commands.autonomous.DriveStraightForDistanceCommand;
import frc.team4069.robot.commands.autonomous.RotateToAngleWithGyroCommand;

public class HookArmCommandGroup extends CommandGroup {

    public HookArmCommandGroup() {
        addSequential(new DeployArmCommand());
        addSequential(new DriveStraightForDistanceCommand(1.5, 0.2));
        addSequential(new WaitCommand(1.5));
        addSequential(new DriveStraightForDistanceCommand(-1.5, 0.2));
        addSequential(new RetractArmCommand());
        addSequential(new DriveStraightForDistanceCommand(1.5, 0.2));
        addSequential(new RotateToAngleWithGyroCommand(30));
    }
}
