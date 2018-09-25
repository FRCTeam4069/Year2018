package frc.team4069.robot.commands.vacuum;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.CommandBase;

public class SetVacuumSpeedCommand extends CommandBase {
    
	private double speed;
	
	public SetVacuumSpeedCommand(double speed) {
        requires(vacuum);
		this.speed = speed;
    }

    @Override
    protected void initialize() {
        SmartDashboard.putBoolean("Vacuum enabled", true);
        vacuum.setConstantSpeed(speed);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
