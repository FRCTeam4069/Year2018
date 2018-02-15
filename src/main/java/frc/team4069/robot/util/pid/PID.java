package frc.team4069.robot.util.pid;

// Known working implementation of PID
// Thanks to 1458 for the original implementation
public class PID {

    private PIDConstants constants;
    private double target;
    private double deadband;

    private double integral = 0;
    private double lastTime = Double.NaN;
    private double lastError = Double.NaN;

    private double lastDerivative = Double.NaN;

    public PID(PIDConstants constants, double target, double deadband) {
        this.constants = constants;
        this.target = target;
        this.deadband = deadband;
    }

    public PID(PIDConstants constants, double target) {
        this(constants, target, 0.01);
    }

    public double update(double value) {
        return update(value, Double.NaN);
    }

    public double update(double value, double derivative) {

        double error = target - value;

        // Calculate delta time
        double dt = (System.currentTimeMillis() / 1000.0) - lastTime;

        double _derivative;

        if(derivative == Double.NaN) {
            if(lastTime == Double.NaN || lastError == Double.NaN) {
                _derivative = 0.0;
            }else {
                _derivative = (error - lastError) / dt; // Calculate the derivative if we're past the first iteration
            }
        }else {
            _derivative = derivative;
        }


        integral += error * dt;

        double output = constants.getkP() * error
                + constants.getkI() * integral
                - constants.getkD() * _derivative;

        lastDerivative = _derivative;
        lastError = error;
        lastTime = System.currentTimeMillis() / 1000.0;

        return output;
    }

    public void setConstants(PIDConstants constants) {
        this.constants = constants;

        integral = 0;
        lastTime = Double.NaN;
        lastError = Double.NaN;
        lastDerivative = Double.NaN;
    }

    public PIDConstants getConstants() {
        return constants;
    }

    public double getTarget() {
        return target;
    }

    public boolean isAtTargetP() {
        return Math.abs(lastError) < deadband;
    }

    public boolean isAtTargetD() {
        return Math.abs(lastDerivative) < (deadband / 10.0);
    }

    public boolean isAtTarget() {
        return isAtTargetD() && isAtTargetP();
    }
}
