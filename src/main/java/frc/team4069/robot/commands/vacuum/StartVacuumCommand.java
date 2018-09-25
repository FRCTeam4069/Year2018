package frc.team4069.robot.commands.vacuum;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.CommandBase;

public class StartVacuumCommand extends CommandBase {
    public StartVacuumCommand() {
        requires(vacuum);
    }

    @Override
    protected void initialize() {
        SmartDashboard.putBoolean("Vacuum enabled", true);
        vacuum.start();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
