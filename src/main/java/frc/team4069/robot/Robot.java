package frc.team4069.robot;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.vision.ThreadArduinoGyro;
import frc.team4069.robot.vision.ThreadLIDAR;
import frc.team4069.robot.vision.ThreadVideoCapture;
import frc.team4069.robot.vision.ThreadVisionProcessor;

public class Robot extends IterativeRobot {

    public boolean ON_RED_SIDE_OF_FIELD = false;
    public ThreadArduinoGyro arduino_thread_instance;
    public ThreadLIDAR lidar_instance;
    private ThreadVideoCapture video_capture_instance;
    private Thread VideoCaptureThreadHandle;
    private ThreadVisionProcessor vision_processor_instance;
    private Thread VisionProcessorThreadHandle;
    private Thread arduinoThreadHandle;
    private Thread lidarThreadHandle;

    private long mLastDashboardUpdateTime = 0;

    private Scheduler scheduler;

    @Override
    public void robotInit() {
        super.robotInit();

        // Initialize the subsystems
//        CommandBase.init();

        // Set up the input class
//        Input.init();

        // Configure the vision threads (disabled for now)
        // VisionData.configureVision();

        // Get the scheduler
        scheduler = Scheduler.getInstance();

        // Vision initializers
        lidar_instance = new ThreadLIDAR();
        lidarThreadHandle = new Thread(lidar_instance);
        lidarThreadHandle.start();
        video_capture_instance = new ThreadVideoCapture();
        VideoCaptureThreadHandle = new Thread(video_capture_instance);
        VideoCaptureThreadHandle.start();
        video_capture_instance.Enable();
        vision_processor_instance = new ThreadVisionProcessor(video_capture_instance,
                VideoCaptureThreadHandle, this);
        VisionProcessorThreadHandle = new Thread(vision_processor_instance);
        VisionProcessorThreadHandle.start();
        arduino_thread_instance = new ThreadArduinoGyro();
        arduinoThreadHandle = new Thread(arduino_thread_instance);
    }

//    @Override
//    public void autonomousInit() {
//        super.autonomousInit();
//        // Drive forward and turn towards the switch
//        scheduler.add(new ZeroElevatorCommand());
//    }
//
//    @Override
//    public void teleopInit() {
//        super.teleopInit();
//        // Remove all commands from the scheduler so no autonomous tasks continue running
//        scheduler.removeAll();
//        // Add an operator control command group to the scheduler, which should never exit
//        scheduler.add(new OperatorControlCommandGroup());
//    }
//
//    @Override
//    public void disabledInit() {
//        // Reset the state of the elevator subsystem so that it doesn't take off when next we enable
//        ElevatorSubsystem.getInstance().reset();
//    }

    // During all phases, run the command scheduler
    private void universalPeriodic() {
        sendDataToSmartDashboard();
//        scheduler.run();
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

    /**
     * Update smart dashboard every 1 second
     */
    private void sendDataToSmartDashboard() {
        long deltat = System.currentTimeMillis() - mLastDashboardUpdateTime;
        if (deltat > 1000) {
            SmartDashboard
                    .putNumber("AUTOTARGET XPOS: ", vision_processor_instance.cregions.mXGreenLine);
            SmartDashboard.putNumber("Auto TARGET Enabled: ",
                    vision_processor_instance.cregions.mTargetVisible);
            SmartDashboard.putNumber("XCENTER",
                    vision_processor_instance.cregions.mXGreenLine); // .lastXCenter);
            SmartDashboard
                    .putNumber("NumContours:", vision_processor_instance.cregions.mContours.size());
            SmartDashboard.putNumber("MAPPED:", vision_processor_instance.lastMapped);
            mLastDashboardUpdateTime = System.currentTimeMillis();
            SmartDashboard.putNumber("LAST HEADING:", arduino_thread_instance.lastHeading);
            SmartDashboard.putNumber("LIDAR Angle:", lidar_instance.lastAngle);
            SmartDashboard.putNumber("LIDAR SS", lidar_instance.lastSignalStrength);
            SmartDashboard.putNumber("LIDAR distance", lidar_instance.lastDistance);
            SmartDashboard.putNumber("LIDAR status:", lidar_instance.lastStatus);
            SmartDashboard.putString("LIDAR LAST ERROR", lidar_instance.lastError);
            SmartDashboard.putString("LIDARMessage:", lidar_instance.lastMessage);
            SmartDashboard.putString("GYRO Last Error", arduino_thread_instance.lastError);
            SmartDashboard.putString("GYRO Message", arduino_thread_instance.lastMessage);
        }
    }
}
