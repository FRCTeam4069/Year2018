package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.motors.TalonSRXMotor;

public class WinchSubsystem extends SubsystemBase {
    private static WinchSubsystem instance;

    private TalonSRXMotor talon;

    private WinchSubsystem() {
        talon = new TalonSRXMotor(10, 17);
    }

    public void start(boolean reversed) {
        talon.set(ControlMode.PercentOutput, reversed ? -1 : 1);
    }

    public void stop() {
        talon.stop();
    }

    public static WinchSubsystem getInstance() {
        if(instance == null) {
            instance = new WinchSubsystem();
        }

        return instance;
    }
}
