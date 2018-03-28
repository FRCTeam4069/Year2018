package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class VacuumSubsystem extends SubsystemBase {

    private static VacuumSubsystem instance;

    private TalonSRXMotor talon;

    private VacuumSubsystem() {
        talon = new TalonSRXMotor(IOMapping.VACUUM_CAN_BUS, true);
//        talon.config_kP(0, 1.0, 0);
//        talon.config_kF(0, 0.9, 0);
//        talon.configAllowableClosedloopError(0, 1000, 0);
    }

    public void start() {
        talon.set(ControlMode.PercentOutput, 1);
//        talon.set(ControlMode.Current, 13 * 30);
    }

    public void stop() {
        talon.stop();
    }

    public boolean isStarted() {
        return talon.isStarted();
    }

    public static VacuumSubsystem getInstance() {
        if (instance == null) {
            instance = new VacuumSubsystem();
        }

        return instance;
    }

    @Override
    public void reset() {
        talon.stop();
    }
}
