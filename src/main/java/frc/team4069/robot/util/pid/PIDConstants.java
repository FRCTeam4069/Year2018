package frc.team4069.robot.util.pid;

// Wrapper class for PID gains
public class PIDConstants {
    private final double kP;
    private final double kI;
    private final double kD;

    public PIDConstants(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }


    public double getkP() {
        return kP;
    }

    public double getkI() {
        return kI;
    }

    public double getkD() {
        return kD;
    }
}
