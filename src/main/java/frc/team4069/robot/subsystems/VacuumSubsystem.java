package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Solenoid;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class VacuumSubsystem extends SubsystemBase {

    private static VacuumSubsystem instance;

    private TalonSRXMotor vacuumTalon;
    //private Solenoid vacuumSolenoid;

    private VacuumSubsystem() {
        vacuumTalon = new TalonSRXMotor(IOMapping.VACUUM_CAN_BUS, true);
        //vacuumSolenoid = new Solenoid(IOMapping.VACUUM_SOLENOID_CAN_BUS, IOMapping.SOLENOID_CHANNEL);
    }

    public void start() {
        vacuumTalon.set(ControlMode.PercentOutput, 1);
        //vacuumSolenoid.set(true);
    }

    public void stop() {
        vacuumTalon.stop();
        //vacuumSolenoid.set(false);
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
