package frc.team4069.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4069.robot.Robot;
import frc.team4069.robot.subsystems.ArmSubsystem;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.subsystems.ElevatorSubsystem;
import frc.team4069.robot.subsystems.VacuumSubsystem;
import frc.team4069.robot.subsystems.WinchSubsystem;

// A generic command class that contains references to all of the subsystems and initializes them
public abstract class CommandBase extends Command {

    // Instances of each of the subsystems
    protected static DriveBaseSubsystem driveBase;
    protected static ElevatorSubsystem elevator;
    protected static ArmSubsystem arm;
    protected static VacuumSubsystem vacuum;
    protected static WinchSubsystem winch;

    private static Robot mRobot;

    // An function that handles initialization of subsystems
    public static void init(Robot robot) {
        mRobot = robot;

        // Get the singleton instance of each of the subsystems
        driveBase = DriveBaseSubsystem.getInstance();
        elevator = ElevatorSubsystem.getInstance();
        arm = ArmSubsystem.getInstance();
        vacuum = VacuumSubsystem.getInstance();
        winch = WinchSubsystem.getInstance();
    }

    // Accessor for the gyro angle
    protected static double getGyroAngle() {
        return mRobot.threadGyroInstance.lastHeading;
    }
}
