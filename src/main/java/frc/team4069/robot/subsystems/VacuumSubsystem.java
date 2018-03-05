package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class VacuumSubsystem extends SubsystemBase {

    private static VacuumSubsystem instance;

    private TalonSRXMotor vacuumTalon;
    //private Solenoid vacuumSolenoid;
    private TalonSRXMotor vacuumSolenoid;

    private VacuumSubsystem() {
        vacuumTalon = new TalonSRXMotor(IOMapping.VACUUM_CAN_BUS, true);
        //vacuumSolenoid = new Solenoid(IOMapping.VACUUM_SOLENOID_CAN_BUS, IOMapping.SOLENOID_CHANNEL);
        vacuumSolenoid = new TalonSRXMotor(22);
    }

    public void start() {
        vacuumTalon.set(ControlMode.PercentOutput, 1);
        //vacuumSolenoid.set(true);
        vacuumSolenoid.set(ControlMode.PercentOutput, 1.0);
    }

    public void stop() {
        vacuumTalon.stop();
        //vacuumSolenoid.set(false);
        vacuumSolenoid.set(ControlMode.PercentOutput, 0);
    }

    public boolean isStarted() {
        return vacuumTalon.isStarted();
    }

    public static VacuumSubsystem getInstance() {
        if (instance == null) {
            instance = new VacuumSubsystem();
        }

        return instance;
    }

    @Override
    public void reset() {
        vacuumTalon.stop();
    }
}
