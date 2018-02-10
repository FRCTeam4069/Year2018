package frc.team4069.robot.vision;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

public class ThreadGyro implements Runnable {

    public double lastHeading = -1;

    private ADXRS450_Gyro gyro;

    public ThreadGyro() {
        gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        gyro.reset();
    }

    public void run() {
        while (true) {
            lastHeading = gyro.getAngle();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

