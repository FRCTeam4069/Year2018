package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class ArmSubsystem extends SubsystemBase {
    private TalonSRXMotor talon;
    
    private static ArmSubsystem instance;


    public static final int MAX_POSITION_TICKS = 2700;
    
    private ArmSubsystem() {
        talon = new TalonSRXMotor(IOMapping.ARM_CAN_BUS);

        talon.config_kF(1, 1, 0);
        talon.config_kP(1, 0.5, 0);

        talon.configMotionCruiseVelocity(800, 0);
        talon.configMotionAcceleration(400, 0);
    }
    
    public void start(boolean reversed) {
        talon.set(ControlMode.PercentOutput, reversed ? -0.1 : 0.2);
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

    @Override
    public void reset() {
        talon.stop();
        talon.setSelectedSensorPosition(0, 0, 0);
    }

    public static ArmSubsystem getInstance() {
        if(instance == null) {
            instance = new ArmSubsystem();
        }
        return instance;
    }
}
