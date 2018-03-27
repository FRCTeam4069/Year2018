package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;
import frc.team4069.robot.motors.PID;
import frc.team4069.robot.subsystems.ElevatorSubsystem;

public class OperatorControlElevatorCommand extends CommandBase {

    private PID slowDownLowerPID, slowDownUpperPID;

    private double maxSpeedGoingUp = 0.7;
    private double maxSpeedGoingDown = 0.4;

    public OperatorControlElevatorCommand() {
        slowDownLowerPID = new PID(0.0002, 0.0, 0.0000005);
        slowDownLowerPID.setOutputCap(1.0);
        slowDownLowerPID.setTarget(0);
        slowDownUpperPID = new PID(0.0002, 0.0, 0.0000005);
        slowDownUpperPID.setOutputCap(1.0);
        slowDownUpperPID.setTarget(ElevatorSubsystem.MAX_POSITION_TICKS);
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
        //System.out.println("Joystick axis: " + elevatorAxis);

        // Scale it down more if we're in the bottom 50 centimeters
        boolean limitSwitchPressed = elevator.isLimitSwitchPressed();
        //System.out.println(limitSwitchPressed);
        double position = elevator.getPosition();
        double speedFactor = lerp(0.25, 1, 0, -10000, position);
        if (speedFactor > 1) {
            speedFactor = 1;
        } else if (speedFactor < 0.25) {
            speedFactor = 0.25;
        }
        if (elevatorAxis < 0) {
            double slowDownSpeedFactorUpper = -slowDownUpperPID.getMotorOutput(position);
            elevator.setConstantSpeed(
                    elevatorAxis * speedFactor * slowDownSpeedFactorUpper * maxSpeedGoingUp);
        } else {
            if (limitSwitchPressed) {
                elevator.setConstantSpeed(0);
            }
            if (position < 0) {
                double slowDownSpeedFactorLower = slowDownLowerPID.getMotorOutput(position);
                elevator.setConstantSpeed(
                        elevatorAxis * slowDownSpeedFactorLower * maxSpeedGoingDown);
            }
        }
        double dpadValue = Input.getOperatorDirectionalPad();
        /*if (dpadValue == 0.0) {
            double newPosition = elevator.higherPreset();
            elevator.setPosition(newPosition);
        }

        if (dpadValue == 180.0) {
            double newPosition = elevator.lowerPreset();
            elevator.setPosition(newPosition);
        }*/
    }

    private double lerp(double a, double b, double a2, double b2, double c) {
        double x = (c - a2) / (b2 - a2);
        return x * b + (1 - x) * a;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
