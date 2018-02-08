package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class ArmSubsystem extends SubsystemBase {
    private TalonSRXMotor talon;
    
    private static ArmSubsystem instance;
    
    private ArmSubsystem() {
        talon = new TalonSRXMotor(IOMapping.ARM_CAN_BUS);
    }
    
    public void start() {
        talon.set(ControlMode.PercentOutput, 0.75);
    }
    
    public void stop() {
        talon.stop();
    }

    public void setPosition(double position) {
        talon.set(ControlMode.Position, position);
    }

    public static ArmSubsystem getInstance() {
        if(instance == null) {
            instance = new ArmSubsystem();
        }
        return instance;
    }
}
