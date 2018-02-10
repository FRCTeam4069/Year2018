package frc.team4069.robot.commands.arm;

import frc.team4069.robot.commands.CommandBase;

public class StopArmCommand extends CommandBase {

    public StopArmCommand() {
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.stop();
        System.out.println("Arm Position: " + arm.getPosition());
        arm.setPosition(arm.getPosition() - 500);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
