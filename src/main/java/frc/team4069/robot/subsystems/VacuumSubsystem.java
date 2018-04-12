package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4069.robot.commands.OperatorControlIntakeCommand;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class VacuumSubsystem extends SubsystemBase {

    // The cutoff value of talon.getOutputCurrent() that tells us we're sealed on a cube
    public static final int SEALED_CUTOFF = 13;
    private static VacuumSubsystem instance;

    private TalonSRXMotor talon;

    private VacuumSubsystem() {
        talon = new TalonSRXMotor(IOMapping.VACUUM_CAN_BUS, true, 21);
    }
	
	public void setConstantSpeed(double speed){
		talon.set(ControlMode.PercentOutput, speed);
	}
	
    public void start() {
        talon.set(ControlMode.PercentOutput, -1);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Vacuum sealed", talon.getOutputCurrent() > VacuumSubsystem.SEALED_CUTOFF);
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
    protected void initDefaultCommand() {
        setDefaultCommand(new OperatorControlIntakeCommand());
    }

    @Override
    public void reset() {
        talon.stop();
        SmartDashboard.putBoolean("Vacuum sealed", false);
    }
}
