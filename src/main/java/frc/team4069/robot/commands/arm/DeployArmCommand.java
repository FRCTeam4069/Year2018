package frc.team4069.robot.commands.arm;

import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.subsystems.ArmSubsystem;

public class DeployArmCommand extends CommandBase {

    public DeployArmCommand() {
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.setPosition(ArmSubsystem.MAX_POSITION_TICKS - 100);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
