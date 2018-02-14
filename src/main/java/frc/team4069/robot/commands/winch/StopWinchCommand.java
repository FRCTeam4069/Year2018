package frc.team4069.robot.commands.winch;

import frc.team4069.robot.commands.CommandBase;

public class StopWinchCommand extends CommandBase {

    public StopWinchCommand() {
        requires(winch);
    }

    @Override
    protected void initialize() {
        winch.stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
