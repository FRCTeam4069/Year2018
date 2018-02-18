package frc.team4069.robot.commands.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class RetractArmCommand extends CommandGroup {

    public RetractArmCommand() {
        addSequential(new StartArmCommand(true));
        addSequential(new WaitCommand(2));
        addSequential(new StopArmCommand());
    }
}
