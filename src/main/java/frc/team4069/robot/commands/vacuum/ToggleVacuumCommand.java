package frc.team4069.robot.commands.vacuum;

import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.team4069.robot.subsystems.VacuumSubsystem;

// This can't use CommandBase, so I need to access the subsystem through its instance getter
// Deal with it.
public class ToggleVacuumCommand extends ConditionalCommand {

    public ToggleVacuumCommand() {
        super(new StopVacuumCommand(), new StartVacuumCommand());
        requires(VacuumSubsystem.getInstance());
    }

    @Override
    protected boolean condition() {
        return VacuumSubsystem.getInstance().isStarted();
    }
}
