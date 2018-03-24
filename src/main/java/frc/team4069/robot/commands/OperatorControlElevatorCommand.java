package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;

public class OperatorControlElevatorCommand extends CommandBase {

    private final double lowSpeed = 0.25;
    private final double highSpeed = 0.5;
    private final double lowSpeedMaxPosition = 0.4;
    private final double highSpeedMinPosition = 0.6;

    // We use this as a latch when controlling the elevator
    // If it's not set, then the drivers are actively controlling the elevator with the stick
    // Otherwise we set the position with MM and flip it
    private boolean set = true;

    public OperatorControlElevatorCommand() {
        requires(elevator);
    }

    @Override
    protected void initialize() {
        elevator.reset();
    }

    @Override
    protected void execute() {

        // Get the axis of the elevator, scale it down so that it's easier to control
        double elevatorAxis = Input.getElevatorAxis();

        // Scale it down more if we're in the bottom 50 centimeters
        double position = elevator.getPositionMeters();
        double speedFactor;
        if (position < 0.4) {
            speedFactor = lowSpeed;
        } else if (position > 0.6) {
            speedFactor = highSpeed;
        } else {
            double maxPositionDelta = highSpeedMinPosition - lowSpeedMaxPosition;
            double actualPositionDelta = position - lowSpeedMaxPosition;
            double maxSpeedDelta = highSpeed - lowSpeed;
            double actualSpeedDelta = maxSpeedDelta * (actualPositionDelta / maxPositionDelta);
            speedFactor = lowSpeed + actualSpeedDelta;
        }
        elevator.setSpeed(elevatorAxis * speedFactor);

        double dpadValue = Input.getOperatorDirectionalPad();
        if (dpadValue == 0.0) {
            double newPosition = elevator.higherPreset();
            elevator.setPosition(newPosition);
        }

        if (dpadValue == 180.0) {
            double newPosition = elevator.lowerPreset();
            elevator.setPosition(newPosition);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
