package frc.team4069.robot.io;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team4069.robot.commands.arm.StartArmCommand;
import frc.team4069.robot.commands.arm.StopArmCommand;
import frc.team4069.robot.commands.elevator.DebugCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.ToggleVacuumCommand;
import frc.team4069.robot.commands.winch.StartWinchCommand;
import frc.team4069.robot.commands.winch.StopWinchCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;


// Class that provides accessors for joystick inputs and maps them to commands
public class Input {

    private static XboxController driveJoystick;
    private static XboxController controlJoystick;

    // Initializer that handles mapping of the joysticks to commands
    public static void init() {
        // Create the joysticks using the port numbers
        driveJoystick = new XboxController(IOMapping.DRIVE_JOYSTICK);
        controlJoystick = new XboxController(IOMapping.CONTROL_JOYSTICK);

        // Map the elevator controls for scale, switch, and exchange
        Button elevatorToSwitch = new JoystickButton(controlJoystick, IOMapping.BUTTON_B);
        elevatorToSwitch.whenPressed(new SetElevatorPositionCommand(Position.SWITCH));
        Button elevatorToExchange = new JoystickButton(controlJoystick, IOMapping.BUTTON_X);
        elevatorToExchange.whenPressed(new SetElevatorPositionCommand(Position.EXCHANGE));
        Button elevatorToScale = new JoystickButton(controlJoystick, IOMapping.BUTTON_Y);
        elevatorToScale.whenPressed(new SetElevatorPositionCommand(Position.SCALE));
        // Run a special command group for elevator intake
        Button elevatorToIntake = new JoystickButton(controlJoystick, IOMapping.BUTTON_A);
//        elevatorToIntake.whenPressed(new SetElevatorPositionCommand(Position.INTAKE));
        elevatorToIntake.whenPressed(new DebugCommand());

        Button winchForward = new JoystickButton(controlJoystick, IOMapping.BUMPER_RIGHT);
        winchForward.whenPressed(new StartWinchCommand());
        winchForward.whenReleased(new StopWinchCommand());

        Button winchBackward = new JoystickButton(controlJoystick, IOMapping.BUMPER_LEFT);
        winchBackward.whenPressed(new StartWinchCommand(true));
        winchBackward.whenReleased(new StopWinchCommand());

        // Stop the vacuum when the start button is pressed
        Button toggleVacuum = new JoystickButton(controlJoystick, IOMapping.BUTTON_BACK);
        toggleVacuum.whenPressed(new ToggleVacuumCommand());

        Button armUp = new JoystickButton(driveJoystick, IOMapping.BUMPER_RIGHT);
        armUp.whenPressed(new StartArmCommand());
        armUp.whenReleased(new StopArmCommand());

        Button armDown = new JoystickButton(driveJoystick, IOMapping.BUMPER_LEFT);
        armDown.whenPressed(new StartArmCommand(true));
        armDown.whenReleased(new StopArmCommand());
    }

    // Accessor for the steering axis on the drive joystick
    public static double getSteeringAxis() {
        // Use the horizontal axis on the left stick
        double axis = driveJoystick.getX(Hand.kLeft);
        // Deadband on the axis ± 0.2 because of joystick drift
        if (Math.abs(axis) > 0 && Math.abs(axis) < 0.2) {
            return 0;
        } else {
            return axis;
        }
    }

    // Accessor for the elevator axis on the drive joystick
    public static double getElevatorAxis() {
        // Use the vertical axis on the right stick
        double axis = controlJoystick.getY(Hand.kRight);

        // Deadband on the axis ± 0.2 because of joystick drift
        if (Math.abs(axis) > 0 && Math.abs(axis) < 0.2) {
            return 0;
        } else {
            return axis;
        }
    }

    // Accessor for the drive speed, using the left and right triggers
    public static double getDriveSpeed() {
        // Right trigger controls forward motion, left trigger controls backward motion
        double forwardMotion = driveJoystick.getRawAxis(IOMapping.RIGHT_TRIGGER_AXIS);
        double backwardMotion = -driveJoystick.getRawAxis(IOMapping.LEFT_TRIGGER_AXIS);
        return backwardMotion + forwardMotion;
    }

    public static boolean getDebugPressed() {
        return driveJoystick.getAButton();
    }

    // Accessor for the directional pad on the joystick
    // Returns an angle in degrees, clockwise from the top of the pad
    // Returns -1 if no input is registered
    public static int getDirectionalPadAngleDegrees() {
        // This functionality is built into the joystick library exactly as described
        return driveJoystick.getPOV(IOMapping.POV_NUMBER);
    }
}
