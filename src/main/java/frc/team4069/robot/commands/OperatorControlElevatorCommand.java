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
        double elevatorAxis = Input.getElevatorAxis() * 0.6;

        if (elevatorAxis != 0.0) { // Driver is using it
            elevator.setSpeed(elevatorAxis);
//            System.out.println(elevatorAxis);
            average.add(elevator.getVelocity());
            set = false;
        } else {
            if (!set) {
                elevator.setPosition((double) elevator.getPosition() - 500);
                set = true;
            }
        }

        if (Input.getDebugPressed()) {
            System.out.println(average
                    .stream()
                    .mapToDouble(a -> a)
                    .average().orElse(0.0));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
