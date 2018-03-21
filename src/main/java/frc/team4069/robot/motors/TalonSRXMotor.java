package frc.team4069.robot.motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import java.util.HashSet;
import java.util.Set;

public class TalonSRXMotor extends TalonSRX {

    private int encoderTicksPerRotation = 4096;
    private Set<TalonSRXMotor> slaves = new HashSet<>();

    public TalonSRXMotor(int deviceNumber, int encoderTicksPerRotation, boolean reversed,
            int... slaveIds) {
        super(deviceNumber);

        this.setInverted(reversed);

        for (int slaveNumber : slaveIds) {
            TalonSRXMotor slave = new TalonSRXMotor(slaveNumber, encoderTicksPerRotation, reversed);
            slave.follow(this);
            slaves.add(slave);
        }

        this.encoderTicksPerRotation = encoderTicksPerRotation;
    }

    public TalonSRXMotor(int deviceNumber) {
        this(deviceNumber, 4096, false);
    }

    public TalonSRXMotor(int deviceNumber, int encoderTicksPerRotation) {
        this(deviceNumber, encoderTicksPerRotation, false);
    }

    public TalonSRXMotor(int deviceNumber, int encoderTicksPerRotation, int[] slaves) {
        this(deviceNumber, encoderTicksPerRotation, false, slaves);
    }

    public TalonSRXMotor(int deviceNumber, int... slaves) {
        this(deviceNumber, 4096, false, slaves);
    }

    public TalonSRXMotor(int deviceNumber, boolean reversed, int... slaves) {
        this(deviceNumber, 4096, reversed, slaves);
    }

    public boolean isStarted() {
        return getControlMode() != ControlMode.Disabled;
    }

    public void stop() {
        this.neutralOutput();
    }

    public void setConstantSpeed(double speed) {
        if (speed < -1) {
            speed = -1;
        }
        if (speed > 1) {
            speed = 1;
        }

        set(ControlMode.PercentOutput, speed);
    }

    public double getDistanceTraveledRotations() {
        double quadPosition = getSensorCollection().getQuadraturePosition();
		System.out.println(quadPosition / encoderTicksPerRotation);
        return quadPosition / encoderTicksPerRotation;
    }
}
