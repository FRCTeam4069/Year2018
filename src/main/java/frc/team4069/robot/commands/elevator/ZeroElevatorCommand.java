package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

// Use our enum values with MotionMagic to move the elevator to predefined locations passed in the constructor
public class ZeroElevatorCommand extends CommandBase {

    // Initializer in which this command requires the elevator subsystem
    public ZeroElevatorCommand() {
        requires(elevator);
    }

    @Override
    protected boolean isFinished() {
        // Run until the limit switch has been pressed
        return elevator.getLimitSwitchClosed();
    }

    @Override
    protected void end() {
        // When the limit switch is pressed, stop the elevator and zero the encoder
        elevator.reset();
    }
}

