package frc.team4069.robot.commands.vacuum;

import frc.team4069.robot.commands.CommandBase;

public class StartVacuumCommand extends CommandBase {
    public StartVacuumCommand() {
        requires(vacuum);
    }

    @Override
    protected void initialize() {
        vacuum.start();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
