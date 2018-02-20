package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import frc.team4069.robot.commands.OperatorControlElevatorCommand;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;
import java.util.Collections;
import java.util.stream.Stream;

public class ElevatorSubsystem extends SubsystemBase {

    // The maximum number of ticks that the elevator motor can safely reach
    public static final int MAX_POSITION_TICKS = -30530;
    // The number of ticks around the edges of the elevator's range in which it starts to slow down
    private static ElevatorSubsystem instance;

    // Motor to control
    private TalonSRXMotor talon;
    // Limit switch at the bottom, used to zero the elevator
    //private DigitalInput limitSwitch;

    private ElevatorSubsystem() {
        //limitSwitch = new DigitalInput(0);
        talon = new TalonSRXMotor(IOMapping.ELEVATOR_CAN_BUS, 1024);

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        /*limitSwitch.requestInterrupts(new InterruptHandlerFunction<Object>() {
            @Override
            public void interruptFired(int interruptAssertedMask, Object param) {
                talon.setSelectedSensorPosition(0, 0, 0);
//                talon.stop();
            }
        });*/

        talon.setSelectedSensorPosition(0, 0, 0);

        //limitSwitch.enableInterrupts();

        // Set closed loop gains
        talon.config_kF(0, 0.5, 0);
        talon.config_kP(0, 0.6, 0);
        talon.config_kD(0, 0.1, 0);

        // Configure motion magic acceleration and cruise velocity so that it actually works
        talon.configMotionCruiseVelocity(3000, 0);
        talon.configMotionAcceleration(1500, 0);

//        talon.setSelectedSensorPosition(0, 0, 0);

        // Soft limits to avoid destruction of hardware
        talon.configReverseSoftLimitThreshold(MAX_POSITION_TICKS, 0);
//        talon.configForwardSoftLimitThreshold(0, 0);
        talon.configReverseSoftLimitEnable(true, 0);
        talon.configForwardSoftLimitEnable(false, 0);

    }

    public static ElevatorSubsystem getInstance() {
        if (instance == null) {
            instance = new ElevatorSubsystem();
        }

        return instance;
    }

    public int getPosition() {
        return talon.getSelectedSensorPosition(0);
    }

    // Set the position of the elevator using one of the presets
    public void setPosition(Position position) {
        set(ControlMode.MotionMagic, position.getTicks());
    }

    // Set the position of the elevator using a custom value
    public void setPosition(double customPosition) {
        set(ControlMode.MotionMagic, customPosition);
    }

    // Set the position of the elevator using one of the presets
    public void setSpeed(double speed) {
        // Store the sign and multiply it by the low pass filter output to keep the direction
        set(ControlMode.PercentOutput, speed);
    }

    public void stop() {
        talon.stop();
    }

    private void set(ControlMode mode, double value) {
        talon.set(mode, value);
    }

    // Get the state of the limit switch at the bottom of the elevator
    public boolean getLimitSwitchClosed() {
        return false;/*limitSwitch.get();*/
    }

    public int getVelocity() {
        return talon.getSelectedSensorVelocity(0);
    }

    @Override
    public void reset() {
        talon.stop();
        talon.setSelectedSensorPosition(0, 0, 0);
    }

    public double higherPreset() {
        double pos = getPosition();

        return Stream.of(Position.values())
                .filter(position -> {
                    double tolerance = Math.abs(position.getTicks()) - Math.abs(pos);
                    return tolerance >= 500 && Math.abs(position.getTicks()) > Math.abs(pos);
                })
                .findFirst()
                .map(Position::getTicks)
                .orElse((int)pos);
    }

    public double lowerPreset() {
        double pos = getPosition();

//        List<Position> positions = Arrays.asList(Position.values());
//        Collections.reverse(positions);
//
//        for(Position position : positions) {
//            if(Math.abs(position.getTicks()) < Math.abs(pos)) {
//                return position.getTicks();
//            }
//        }

        return Stream.of(Position.values())
                .sorted(Collections.reverseOrder())
                .filter(position -> Math.abs(position.getTicks()) < Math.abs(pos))
                .findFirst()
                .map(Position::getTicks)
                .orElse((int)pos);

//        return pos;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new OperatorControlElevatorCommand());
    }

    // Enum that holds tick values for the various positions that the elevator must go to
    public enum Position {
        MINIMUM(0),
        EXCHANGE(-3000),
        INTAKE(-5500),
        SWITCH(-15000),
        SCALE(MAX_POSITION_TICKS + 100);

        private int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }
}
