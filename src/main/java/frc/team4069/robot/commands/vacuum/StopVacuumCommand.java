package frc.team4069.robot.commands.vacuum;

import frc.team4069.robot.commands.CommandBase;

public class StopVacuumCommand extends CommandBase {

    public StopVacuumCommand() {
        requires(vacuum);
    }

    @Override
    protected void initialize() {
        vacuum.stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
