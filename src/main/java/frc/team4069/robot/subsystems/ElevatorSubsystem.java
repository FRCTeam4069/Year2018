package frc.team4069.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.team4069.robot.io.IOMapping;
import frc.team4069.robot.motors.TalonSRXMotor;

public class ElevatorSubsystem extends SubsystemBase {

    // The maximum number of ticks that the elevator motor can safely reach
    public static final int MAX_POSITION_TICKS = -26901;
    // The number of ticks around the edges of the elevator's range in which it starts to slow down
    private static ElevatorSubsystem instance;

    // Motor to control
    private TalonSRXMotor talon;

    private ElevatorSubsystem() {
        talon = new TalonSRXMotor(IOMapping.ELEVATOR_CAN_BUS);

        // Stop the elevator from coasting when the talon is stopped (probably)
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

        // Set the feed-forward gain
        talon.config_kF(0, 0.5, 0);
        talon.config_kP(0, 0.1, 0);

        talon.configMotionCruiseVelocity(1500, 0);

//        talon.setSelectedSensorPosition(0, 0, 0);

//        talon.configReverseSoftLimitThreshold(MAX_POSITION_TICKS, 0);
//        talon.configForwardSoftLimitThreshold(0, 0);
//        talon.configReverseSoftLimitEnable(false, 0);
//        talon.configForwardSoftLimitEnable(false, 0);

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

    public void stop() {
        talon.stop();
    }

    public void set(ControlMode mode, double speed) {
        talon.set(mode, speed);
    }

    public void reset() {
        talon.stop();
        talon.setSelectedSensorPosition(0, 0, 0);
    }

    // Enum that holds tick values for the various positions that the elevator must go to
    public enum Position {
        INTAKE(-6938),
        EXCHANGE(-3000),
        SWITCH(15000),
        SCALE(MAX_POSITION_TICKS);

        private int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }

        public int getTicks() {
            return ticks;
        }
    }
}
