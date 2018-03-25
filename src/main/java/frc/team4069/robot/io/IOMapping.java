package frc.team4069.robot.io;

// A class containing static constants that contain the port numbers for connected devices
public class IOMapping {

	// Power distribution channel for elevator
	public static final int ELEVATOR_POWER_CHANNEL = 9;
    // CAN bus port for the climber hook motor
    public static final int ELEVATOR_CAN_BUS = 16;
    public static final int VACUUM_CAN_BUS = 21;
    public static final int WINCH_CAN_BUS = 10;
    public static final int ARM_CAN_BUS = 24;
    public static final int VACUUM_SOLENOID_CAN_BUS = 5;
    // CAN bus ports for the drive motors
    public static final int LEFT_DRIVE_CAN_BUS = 12;
    public static final int RIGHT_DRIVE_CAN_BUS = 19;
    // Limit switch digital input numbers
    public static final int ELEVATOR_LIMIT_SWITCH = 7;

    public static final int SOLENOID_CHANNEL = 0;

    // The port numbers of the joysticks
    static final int DRIVE_JOYSTICK = 0;
    static final int CONTROL_JOYSTICK = 1;

    // Analog axes
    static final int LEFT_STICK_HORIZONTAL_AXIS = 0;
    static final int LEFT_STICK_VERTICAL_AXIS = 1;
    static final int LEFT_TRIGGER_AXIS = 2;
    static final int RIGHT_TRIGGER_AXIS = 3;
    static final int RIGHT_STICK_HORIZONTAL_AXIS = 4;
    static final int RIGHT_STICK_VERTICAL_AXIS = 5;

    // The number of the POV (directional pad)
    static final int POV_NUMBER = 0;


    // Joystick buttons
    static final int BUTTON_A = 1;
    static final int BUTTON_B = 2;
    static final int BUTTON_X = 3;
    static final int BUTTON_Y = 4;
    static final int BUMPER_LEFT = 5;
    static final int BUMPER_RIGHT = 6;
    static final int BUTTON_BACK = 7;
    static final int BUTTON_START = 8;
}
