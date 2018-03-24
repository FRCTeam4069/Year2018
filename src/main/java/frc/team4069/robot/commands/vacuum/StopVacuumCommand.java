package frc.team4069.robot.commands.vacuum;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.CommandBase;

public class StopVacuumCommand extends CommandBase {

    public StopVacuumCommand() {
        requires(vacuum);
    }

    @Override
    protected void initialize() {
        vacuum.stop();
        SmartDashboard.putBoolean("Vacuum enabled", false);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
