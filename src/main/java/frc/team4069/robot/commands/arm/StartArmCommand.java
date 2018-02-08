package frc.team4069.robot.commands.arm;

import frc.team4069.robot.commands.CommandBase;

//TODO: Move the limit switch to the arm and use it like that
public class StartArmCommand extends CommandBase {
    public StartArmCommand() {
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.setPosition(1230);
        // 150 is the "down" position
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
