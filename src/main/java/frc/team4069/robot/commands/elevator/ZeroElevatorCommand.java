package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

public class ZeroElevatorCommand extends CommandBase {

    // Initializer in which this command requires the elevator subsystem
    public ZeroElevatorCommand() {
        requires(elevator);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        // When the limit switch is pressed, stop the elevator and zero the encoder
        elevator.reset();
    }
}

