package frc.team4069.robot.vision;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import frc.team4069.robot.subsystems.DriveBaseSubsystem;

public class ThreadGyro implements Runnable {

    public double lastHeading = -1;

    private ADXRS450_Gyro gyro;

    private DriveBaseSubsystem db;
    private long startTime;

    public ThreadGyro() {
        gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        gyro.reset();
        db = DriveBaseSubsystem.getInstance();
        startTime = System.currentTimeMillis();
    }

    public void run() {
        while (true) {
            lastHeading = gyro.getAngle();
            if (System.currentTimeMillis() <= startTime + 5000) {
                System.out.println("distance traveled: " + db.getDistanceTraveledMeters());
                System.out.println("time in ms: " + System.currentTimeMillis());
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

