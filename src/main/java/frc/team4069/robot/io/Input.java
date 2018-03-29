package frc.team4069.robot.io;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team4069.robot.commands.ElevatorIntakeCommandGroup;
import frc.team4069.robot.commands.arm.DeployArmCommand;
import frc.team4069.robot.commands.arm.StartArmCommand;
import frc.team4069.robot.commands.arm.StopArmCommand;
import frc.team4069.robot.commands.drive.ToggleDrivePrecisionModeCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.ToggleVacuumCommand;
import frc.team4069.robot.commands.winch.StartWinchCommand;
import frc.team4069.robot.commands.winch.StopWinchCommand;
import frc.team4069.robot.commands.spline.FollowSplinePathCommand;
import frc.team4069.robot.commands.DriveForwardVacuumCommandGroup;
import frc.team4069.robot.commands.ApproachScaleCommandGroup;
import frc.team4069.robot.commands.InlineCommandGroup;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;
import frc.team4069.robot.spline.SplinePath;


// Class that provides accessors for joystick inputs and maps them to commands
public class Input {

    private static XboxController driveJoystick;
    private static XboxController controlJoystick;

    // Initializer that handles mapping of the joysticks to commands
    public static void init() {
        // Create the joysticks using the port numbers
        driveJoystick = new XboxController(IOMapping.DRIVE_JOYSTICK);
        controlJoystick = new XboxController(IOMapping.CONTROL_JOYSTICK);
		
        Button startWinch = new JoystickButton(controlJoystick, IOMapping.BUMPER_LEFT);
        startWinch.whenPressed(new StartWinchCommand());
        startWinch.whenReleased(new StopWinchCommand());
		
		Button splineToExchangeFarSwitch = new JoystickButton(controlJoystick, IOMapping.BUTTON_X);
		splineToExchangeFarSwitch.whenPressed(new InlineCommandGroup(new FollowSplinePathCommand(SplinePath.splinePathTeleopExchangeFarSwitchMirror), new ToggleDrivePrecisionModeCommand()));
		
		Button splineTeleopScale = new JoystickButton(driveJoystick, IOMapping.BUTTON_X);
		splineTeleopScale.whenPressed(new InlineCommandGroup(new ApproachScaleCommandGroup(), new ToggleDrivePrecisionModeCommand()));
		
		Button splineToExchange = new JoystickButton(controlJoystick, IOMapping.BUTTON_Y);
		splineToExchange.whenPressed(new InlineCommandGroup(new FollowSplinePathCommand(SplinePath.splinePathTeleopExchangeMirror), new ToggleDrivePrecisionModeCommand()));
		
		Button splineToExchangeFarScale = new JoystickButton(controlJoystick, IOMapping.BUTTON_A);
		splineToExchangeFarScale.whenPressed(new InlineCommandGroup(new FollowSplinePathCommand(SplinePath.splinePathTeleopExchangeFarScaleMirror, 25, 1.0, 1.0), new ToggleDrivePrecisionModeCommand()));
		
		Button elevatorPortal = new JoystickButton(controlJoystick, IOMapping.BUTTON_B);
		elevatorPortal.whenPressed(new SetElevatorPositionCommand(-7217));
		
        // Stop the vacuum when the start button is pressed
        Button toggleVacuum = new JoystickButton(controlJoystick, IOMapping.BUMPER_RIGHT);
        toggleVacuum.whenPressed(new ToggleVacuumCommand());

        Button armDown = new JoystickButton(driveJoystick, IOMapping.BUTTON_A);
        armDown.whenPressed(new StartArmCommand(true));
        armDown.whenReleased(new StopArmCommand());

        Button deployArm = new JoystickButton(driveJoystick, IOMapping.BUTTON_Y);
        deployArm.whenPressed(new DeployArmCommand());

        Button togglePrecisionMode = new JoystickButton(driveJoystick, IOMapping.BUTTON_START);
        togglePrecisionMode.whenPressed(new ToggleDrivePrecisionModeCommand());

        Button pickupCube = new JoystickButton(driveJoystick, IOMapping.BUTTON_B);
        pickupCube.whenPressed(new DriveForwardVacuumCommandGroup(0.5));

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
        if (driveJoystick.getRawButton(IOMapping.BUMPER_LEFT)) {
            return -DriveBaseSubsystem.SLOW_SPEED;
        }
        if (driveJoystick.getRawButton(IOMapping.BUMPER_RIGHT)) {
            return DriveBaseSubsystem.SLOW_SPEED;
        }
        double forwardMotion = driveJoystick.getRawAxis(IOMapping.RIGHT_TRIGGER_AXIS);
        double backwardMotion = -driveJoystick.getRawAxis(IOMapping.LEFT_TRIGGER_AXIS);
        return backwardMotion + forwardMotion;
    }

    public static boolean getDebugPressed() {
        return driveJoystick.getAButton();
    }

    public static int getOperatorDirectionalPad() {
        return controlJoystick.getPOV(IOMapping.POV_NUMBER);
    }

    // Accessor for the directional pad on the joystick
    // Returns an angle in degrees, clockwise from the top of the pad
    // Returns -1 if no input is registered
    public static int getDriveDirectionalPadDegrees() {
        // This functionality is built into the joystick library exactly as described
        return driveJoystick.getPOV(IOMapping.POV_NUMBER);
    }
}
