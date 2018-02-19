package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Use our enum values with MotionMagic to move the elevator to predefined locations passed in the constructor
public class SetElevatorPositionCommand extends CommandBase {

    private Position position;
    private boolean blocking;

    public SetElevatorPositionCommand(Position position, boolean blocking) {
        this.position = position;
        this.blocking = blocking;
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.setPosition(position);
    }

    @Override
    protected boolean isFinished() {
        if (!blocking) {
            return true;
        }

        double pos = elevator.getPosition();
        if (position == Position.MINIMUM) {
            return Math.abs(pos) <= 100;
        }

        double tolerance = Math.abs(position.getTicks()) - Math.abs(pos);

        return tolerance <= 1000;
    }
}
