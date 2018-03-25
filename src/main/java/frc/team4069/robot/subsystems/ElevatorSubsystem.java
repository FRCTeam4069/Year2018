package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.team4069.robot.commands.OperatorControlElevatorCommand;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;
import frc.team4069.robot.motors.PID;
import java.util.Collections;
import java.util.stream.Stream;

public class ElevatorSubsystem extends SubsystemBase {

    // The maximum number of ticks that the elevator motor can safely reach
    public static final int MAX_POSITION_TICKS = -29000;
    // The number of ticks around the edges of the elevator's range in which it starts to slow down
    private static ElevatorSubsystem instance;

    // Motor to control
    private TalonSRXMotor talon;
	
	private DigitalInput limitSwitch;

    private ElevatorSubsystem() {
        talon = new TalonSRXMotor(IOMapping.ELEVATOR_CAN_BUS, 4096, true, 15);
		limitSwitch = new DigitalInput(IOMapping.ELEVATOR_LIMIT_SWITCH);
    }

    public static ElevatorSubsystem getInstance() {
        if (instance == null) {
            instance = new ElevatorSubsystem();
        }
        return instance;
    }

    public double getPosition() {
        return talon.getDistanceTraveledTicks();
    }
	
	public void setConstantSpeed(double speed){
		talon.setConstantSpeed(speed);
	}
	
	public double getPositionTicks(){
		return talon.getDistanceTraveledTicks();
	}
	
	public boolean isLimitSwitchPressed(){
		return limitSwitch.get();
	}

    public void stop() {
        talon.stop();
    }

    @Override
    public void reset() {
        talon.stop();
        talon.setSelectedSensorPosition(0, 0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new OperatorControlElevatorCommand());
    }

    // Enum that holds tick values for the various positions that the elevator must go to
    public enum Position {
        MINIMUM(0),
        EXCHANGE(3000),
        INTAKE(5500),
        SWITCH(15000),
        SCALE(MAX_POSITION_TICKS - 100);

        private int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }
}
