package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

public class StopElevatorCommand extends CommandBase {

    public StopElevatorCommand() {
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
