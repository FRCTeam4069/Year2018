package frc.team4069.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4069.robot.Robot;
import frc.team4069.robot.subsystems.ArmSubsystem;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.subsystems.ElevatorSubsystem;
import frc.team4069.robot.subsystems.VacuumSubsystem;
import frc.team4069.robot.subsystems.WinchSubsystem;
import frc.team4069.robot.vision.ThreadVisionProcessor.ColourRegions;

// A generic command class that contains references to all of the subsystems and initializes them
public abstract class CommandBase extends Command {

    // Instances of each of the subsystems
    protected static DriveBaseSubsystem driveBase;
    protected static ElevatorSubsystem elevator;
    protected static ArmSubsystem arm;
    protected static VacuumSubsystem vacuum;
    protected static WinchSubsystem winch;
    private static Robot mRobot;

    // A function that handles initialization of subsystems
    public static void init(Robot robot) {
        mRobot = robot;
		System.out.println("COMMAND BASE INIT REACHED");
        // Get the singleton instance of each of the subsystems
        driveBase = DriveBaseSubsystem.getInstance();
        elevator = ElevatorSubsystem.getInstance();
        arm = ArmSubsystem.getInstance();
        vacuum = VacuumSubsystem.getInstance();
        winch = WinchSubsystem.getInstance();
        elevator.reset();
    }

    // Accessor for the gyro angle
    protected static double getGyroAngle() {
        return mRobot.threadGyroInstance.lastHeading;
    }

    // Accessor for the colour regions of the vision processor
    protected static ColourRegions getColourRegions() {
        return mRobot.threadVisionProcessorInstance.cregions;
    }

    // Accessor for the Lidar distance directly ahead
    protected static double getDistanceAheadCentimeters() {
        return mRobot.threadLIDARInstance.lastDistance;
    }

    // Accessors for the power cube's position
    protected static int getPowerCubeXPos() {
        return mRobot.threadArmCamera.powerCubeXPos;
    }

    protected static int getPowerCubeYPos() {
        return mRobot.threadArmCamera.powerCubeYPos;
    }

    // Accessors for the dimensions of the arm camera image
    protected static int getArmCameraImageWidth() {
        return mRobot.threadArmCamera.width;
    }

    protected static int getArmCameraImageHeight() {
        return mRobot.threadArmCamera.height;
    }
}
