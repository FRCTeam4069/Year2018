package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;
import java.util.ArrayList;
import java.util.List;

public class OperatorControlElevatorCommand extends CommandBase {

    // We use this as a latch when controlling the elevator
    // If it's not set, then the drivers are actively controlling the elevator with the stick
    // Otherwise we set the position with MM and flip it
    private boolean set = true;
    private List<Integer> average;

    OperatorControlElevatorCommand() {
        requires(elevator);
        average = new ArrayList<>();
    }

    @Override
    protected void execute() {
        // Get the axis of the elevator, scale it down so that it's easier to control
        double elevatorAxis = Input.getElevatorAxis() * 0.5;

        if (elevatorAxis != 0.0) { // Driver is using it
            elevator.setSpeed(elevatorAxis);
            set = false;
        } else {
            if (!set) {
                elevator.setPosition((double) elevator.getPosition() - 500);
                set = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
