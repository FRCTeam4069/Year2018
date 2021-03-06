package frc.team4069.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.commands.OperatorControlCommandGroup;
import frc.team4069.robot.commands.autonomous.AutoMode;
import frc.team4069.robot.commands.autonomous.AutonomousCommandGroup;
import frc.team4069.robot.io.Input;
import frc.team4069.robot.subsystems.ArmSubsystem;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;
import frc.team4069.robot.subsystems.VacuumSubsystem;
import frc.team4069.robot.subsystems.WinchSubsystem;
import frc.team4069.robot.vision.ThreadArmCamera;
import frc.team4069.robot.vision.ThreadGyro;
import frc.team4069.robot.vision.ThreadLIDAR;
import frc.team4069.robot.vision.ThreadVideoCapture;
import frc.team4069.robot.vision.ThreadVisionProcessor;

public class Robot extends IterativeRobot {

    public ThreadGyro threadGyroInstance;
    public ThreadLIDAR threadLIDARInstance;
    public ThreadVisionProcessor threadVisionProcessorInstance;
    public ThreadArmCamera threadArmCamera;
    //    public Thread threadArmCameraHandle;
    private ThreadVideoCapture threadVideoCaptureInstance;
    private Thread threadGyroHandle;
    private Thread threadLIDARHandle;
    private Thread threadVideoCaptureHandle;
    private Thread threadVisionProcessorHandle;
    private long mLastDashboardUpdateTime = 0;

    private Scheduler scheduler;
	
	private static boolean isOperatorControl;

	public static AutoMode autoMode = AutoMode.DOUBLE_SWITCH;

	private SendableChooser<AutoMode> autoChooser;

    @Override
    public void robotInit() {
        super.robotInit();

        // Initialize the subsystems
        CommandBase.init(this);
		
		Input.init();
		
        // Get the scheduler
        scheduler = Scheduler.getInstance();

        // Vision initializers
        threadGyroInstance = new ThreadGyro();
        threadGyroHandle = new Thread(threadGyroInstance);
        threadGyroHandle.start();
        threadLIDARInstance = new ThreadLIDAR();
        threadLIDARHandle = new Thread(threadLIDARInstance);
        threadLIDARHandle.start();
//        threadVideoCaptureInstance = new ThreadVideoCapture();
//        threadVideoCaptureHandle = new Thread(threadVideoCaptureInstance);
//        threadVideoCaptureHandle.start();
//        threadVideoCaptureInstance.Enable();
//        threadVisionProcessorInstance = new ThreadVisionProcessor(threadVideoCaptureInstance,
//                threadVideoCaptureHandle, this);
//        threadVisionProcessorHandle = new Thread(threadVisionProcessorInstance);
//        threadVisionProcessorHandle.start();
//        threadArmCamera = new ThreadArmCamera();
//        threadArmCameraHandle = new Thread(threadArmCamera);
//        threadArmCameraHandle.start();

        // Send the camera feed through networktables
        CameraServer.getInstance().startAutomaticCapture(0);

        SmartDashboard.putNumber("Distance along wall (metres)", 5);
        SmartDashboard.putBoolean("Vacuum enabled", false);
        SmartDashboard.putBoolean("Vacuum sealed", false);

        autoChooser = new SendableChooser();
        autoChooser.addObject("Switch Scale Autonomous", AutoMode.SWITCH_SCALE);
        autoChooser.addObject("Switch Half Scale Autonomous", AutoMode.SWITCH_HALF_SCALE);
        autoChooser.addObject("Double Scale Right Autonomous", AutoMode.DOUBLE_SCALE_RIGHT);
        autoChooser.addObject("Double Scale Left Autonomous", AutoMode.DOUBLE_SCALE_LEFT);
        autoChooser.addObject("Double Scale Right Straight Only Autonomous", AutoMode.DOUBLE_SCALE_RIGHT_STRAIGHT_ONLY);
        autoChooser.addObject("Double Scale Left Straight Only Autonomous", AutoMode.DOUBLE_SCALE_LEFT_STRAIGHT_ONLY);
        autoChooser.addDefault("Double Switch Autonomous", AutoMode.DOUBLE_SWITCH);
        autoChooser.addObject("Drive Straight Autonomous", AutoMode.DRIVE_STRAIGHT);
        autoChooser.addObject("Do Nothing Autonomous", AutoMode.DO_NOTHING);
        SmartDashboard.putData("Autonomous Modes", autoChooser);
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        autoMode = autoChooser.getSelected();
		isOperatorControl = false;
        // Run the autonomous command group, which handles driving, elevator, and vacuum control
        scheduler.add(new AutonomousCommandGroup());
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
		Input.init();
		isOperatorControl = true;
        // Remove all commands from the scheduler so no autonomous tasks continue running
        scheduler.removeAll();
        // Add an operator control command group to the scheduler, which should never exit
        scheduler.add(new OperatorControlCommandGroup());
    }

    @Override
    public void disabledInit() {
        // Reset the state of the elevator subsystem so that it doesn't take off when next we enable
        //ElevatorSubsystem.getInstance().reset();
        ArmSubsystem.getInstance().reset();
        DriveBaseSubsystem.getInstance().reset();
        VacuumSubsystem.getInstance().reset();
        WinchSubsystem.getInstance().reset();

    }

    // During all phases, run the command scheduler
    private void universalPeriodic() {
//        sendDataToSmartDashboard();
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
	
	public static boolean getOperatorControl(){
		return isOperatorControl;
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
