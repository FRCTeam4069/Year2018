package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Use our enum values with MotionMagic to move the elevator to predefined locations passed in the constructor
public class SetElevatorPositionCommand extends CommandBase {
    private Position position;

    public SetElevatorPositionCommand(Position position) {
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
