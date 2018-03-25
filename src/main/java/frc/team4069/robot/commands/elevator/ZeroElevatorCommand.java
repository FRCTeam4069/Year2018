package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

public class ZeroElevatorCommand extends CommandBase {
	
    public ZeroElevatorCommand() {
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.reset();
    }

    @Override
    protected boolean isFinished() {
		return true;
    }
}
