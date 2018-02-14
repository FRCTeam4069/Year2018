package frc.team4069.robot.commands.arm;

import frc.team4069.robot.commands.CommandBase;

//TODO: Move the limit switch to the arm and use it like that
public class StartArmCommand extends CommandBase {

    private boolean reversed;

    public StartArmCommand() {
        this(false);
    }

    public StartArmCommand(boolean reversed) {
        this.reversed = reversed;
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.start(reversed);
        // 150 is the "down" position
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
