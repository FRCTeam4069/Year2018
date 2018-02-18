package frc.team4069.robot.commands.drive;

import frc.team4069.robot.commands.CommandBase;

public class ToggleDrivePrecisionModeCommand extends CommandBase {

    public ToggleDrivePrecisionModeCommand() {
        requires(driveBase);
    }

    @Override
    protected void initialize() {
        driveBase.togglePrecisionMode();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
