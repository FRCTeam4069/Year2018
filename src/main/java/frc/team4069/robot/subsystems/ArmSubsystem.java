package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class ArmSubsystem extends SubsystemBase {
    private TalonSRXMotor talon;
    
    private static ArmSubsystem instance;
    
    private ArmSubsystem() {
        talon = new TalonSRXMotor(IOMapping.ARM_CAN_BUS);

        talon.config_kF(1, 1, 0);
        talon.config_kP(1, 0.5, 0);

    }
    
    public void start() {
        talon.set(ControlMode.PercentOutput, 0.25);
    }
    
    public void stop() {
        talon.stop();
    }

    public void setPosition(double position) {
        talon.set(ControlMode.MotionMagic, position);
    }

    public double getPosition() {
        return talon.getSelectedSensorPosition(0);
    }

    public static ArmSubsystem getInstance() {
        if(instance == null) {
            instance = new ArmSubsystem();
        }
        return instance;
    }
}
