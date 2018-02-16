package frc.team4069.robot.commands.arm;

import frc.team4069.robot.commands.CommandBase;

public class RetractArmCommand extends CommandBase {

    public RetractArmCommand() {
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.setPosition(50);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
