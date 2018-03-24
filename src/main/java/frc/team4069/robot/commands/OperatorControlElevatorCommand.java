package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;

public class OperatorControlElevatorCommand extends CommandBase {

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
        elevator.setSpeed(elevatorAxis * 0.5);

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
