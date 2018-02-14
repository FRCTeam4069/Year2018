package frc.team4069.robot.commands.winch;

import frc.team4069.robot.commands.CommandBase;

public class StartWinchCommand extends CommandBase {

    private boolean reversed;

    public StartWinchCommand() {
        this(false);
    }

    public StartWinchCommand(boolean reversed) {
        this.reversed = reversed;
        requires(winch);
    }

    @Override
    protected void initialize() {
        winch.start(reversed);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
