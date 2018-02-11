package frc.team4069.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.commands.OperatorControlCommandGroup;
import frc.team4069.robot.commands.autonomous.DriveTowardTapeCommand;
import frc.team4069.robot.io.Input;
import frc.team4069.robot.subsystems.ElevatorSubsystem;
import frc.team4069.robot.vision.ThreadGyro;
import frc.team4069.robot.vision.ThreadLIDAR;
import frc.team4069.robot.vision.ThreadVideoCapture;
import frc.team4069.robot.vision.ThreadVisionProcessor;

public class Robot extends IterativeRobot {

    public ThreadGyro threadGyroInstance;
    public ThreadLIDAR threadLIDARInstance;
    public ThreadVisionProcessor threadVisionProcessorInstance;
    private ThreadVideoCapture threadVideoCaptureInstance;
    private Thread threadGyroHandle;
    private Thread threadLIDARHandle;
    private Thread threadVideoCaptureHandle;
    private Thread threadVisionProcessorHandle;

    private long mLastDashboardUpdateTime = 0;

    private Scheduler scheduler;

    @Override
    public void robotInit() {
        super.robotInit();

        // Initialize the subsystems
        CommandBase.init(this);

        // Set up the input class
        Input.init();

        // Configure the vision threads (disabled for now)
//        VisionData.configureVision();
        // Get the scheduler
        scheduler = Scheduler.getInstance();

        // Vision initializers;
        threadGyroInstance = new ThreadGyro();
        threadGyroHandle = new Thread(threadGyroInstance);
        threadGyroHandle.start();
        threadLIDARInstance = new ThreadLIDAR();
        threadLIDARHandle = new Thread(threadLIDARInstance);
        threadLIDARHandle.start();
        threadVideoCaptureInstance = new ThreadVideoCapture();
        threadVideoCaptureHandle = new Thread(threadVideoCaptureInstance);
        threadVideoCaptureHandle.start();
        threadVideoCaptureInstance.Enable();
        threadVisionProcessorInstance = new ThreadVisionProcessor(threadVideoCaptureInstance,
                threadVideoCaptureHandle, this);
        threadVisionProcessorHandle = new Thread(threadVisionProcessorInstance);
        threadVisionProcessorHandle.start();
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        // Run the autonomous command group, which handles driving, elevator, and vacuum control
        scheduler.add(new DriveTowardTapeCommand());
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        // Remove all commands from the scheduler so no autonomous tasks continue running
        scheduler.removeAll();
        // Add an operator control command group to the scheduler, which should never exit
        scheduler.add(new OperatorControlCommandGroup());
    }

    @Override
    public void disabledInit() {
        // Reset the state of the elevator subsystem so that it doesn't take off when next we enable
        ElevatorSubsystem.getInstance().reset();
    }

    // During all phases, run the command scheduler
    private void universalPeriodic() {
        sendDataToSmartDashboard();
        scheduler.run();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
        universalPeriodic();
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
        universalPeriodic();
    }

    // Update smart dashboard every 1 second
    private void sendDataToSmartDashboard() {
        long deltaTime = System.currentTimeMillis() - mLastDashboardUpdateTime;
        if (deltaTime > 1000) {
            mLastDashboardUpdateTime = System.currentTimeMillis();
            SmartDashboard.putNumber("AUTOTARGET XPOS: ",
                    threadVisionProcessorInstance.cregions.mXGreenLine);
            SmartDashboard.putNumber("Auto TARGET Enabled: ",
                    threadVisionProcessorInstance.cregions.mTargetVisible);
            SmartDashboard.putNumber("XCENTER",
                    threadVisionProcessorInstance.cregions.mXGreenLine);
            SmartDashboard.putNumber("NumContours:",
                    threadVisionProcessorInstance.cregions.mContours.size());
            SmartDashboard.putNumber("MAPPED:", threadVisionProcessorInstance.lastMapped);
            SmartDashboard.putNumber("LAST HEADING:", threadGyroInstance.lastHeading);
            SmartDashboard.putNumber("LIDAR Angle:", threadLIDARInstance.lastAngle);
            SmartDashboard.putNumber("LIDAR SS", threadLIDARInstance.lastSignalStrength);
            SmartDashboard.putNumber("LIDAR distance", threadLIDARInstance.lastDistance);
            SmartDashboard.putNumber("LIDAR status:", threadLIDARInstance.lastStatus);
            SmartDashboard.putString("LIDAR LAST ERROR", threadLIDARInstance.lastError);
            SmartDashboard.putString("LIDARMessage:", threadLIDARInstance.lastMessage);
        }
    }
}
