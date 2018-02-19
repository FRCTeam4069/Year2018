package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

public class SetCustomElevatorPositionCommand extends CommandBase {
    private double position;

    public SetCustomElevatorPositionCommand(double position) {
        this.position = position;

        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.setPosition(position);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
