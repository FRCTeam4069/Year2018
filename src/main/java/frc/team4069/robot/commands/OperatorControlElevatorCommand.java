package frc.team4069.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
    protected void execute() {
        // Get the axis of the elevator, scale it down so that it's easier to control
        double elevatorAxis = Input.getElevatorAxis() * 0.6;
        System.out.println(elevatorAxis);
        if (elevatorAxis != 0.0) { // Driver is using it
            elevator.set(ControlMode.PercentOutput, elevatorAxis);
            set = false;
        } else {
            if (!set) {
                elevator.set(ControlMode.MotionMagic, (double) elevator.getPosition() - 500);
                set = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
