package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.motors.TalonSRXMotor;

public class WinchSubsystem extends SubsystemBase {
    private static WinchSubsystem instance;

    private TalonSRXMotor talon;

    private WinchSubsystem() {
        // Stupid workaround because we have 4 god damn int parameters in a row and Java can't differentiate between them and a variadic call
        talon = new TalonSRXMotor(10, new int[] {17});
    }

    public void start(boolean reversed) {
        talon.set(ControlMode.PercentOutput, reversed ? -0.5 : 1);
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

    @Override
    public void reset() {
        talon.stop();
    }
}
