package frc.team4069.robot.util.pid;

// Thanks to 1458 for the original implementation
public class PID {

    // Our gains, target, and deadband
    private PIDConstants constants;
    private double target;
    private double deadband;

    // Our moving values, integral accumulator, and previous iteration values
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

    /**
     * Shorthand for default NaN derivative value
     *
     * @param value The input value
     * @return The output after PID
     */
    public double update(double value) {
        return update(value, Double.NaN);
    }

    /**
     * Calculates new output value for PID
     *
     * @param value The input value
     * @param derivative The derivative to work with, or NaN
     * @return The output after PID
     */
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

    /**
     * Change the PID gains for this controller
     *
     * @param constants The new gains to use
     */
    public void setConstants(PIDConstants constants) {
        this.constants = constants;

        integral = 0;
        lastTime = Double.NaN;
        lastError = Double.NaN;
        lastDerivative = Double.NaN;
    }

    /**
     * Get the current gains in use for this controller
     *
     * @return The current PID gains
     */
    public PIDConstants getConstants() {
        return constants;
    }

    public double getTarget() {
        return target;
    }

    /**
     * Returns whether the controller is currently at the target (error is low)
     *
     * @return true if the error is within the deadband
     */
    public boolean isAtTargetP() {
        return Math.abs(lastError) < deadband;
    }

    /**
     * Returns whether the controller is currently in a steady state
     *
     * @return true if the last derivative is within the deadband
     */
    public boolean isAtTargetD() {
        return Math.abs(lastDerivative) < (deadband / 10.0);
    }

    /**
     * Returns whether the controller is currently in a steady state at the target
     *
     * @return true if both error and derivative are within deadband
     */
    public boolean isAtTarget() {
        return isAtTargetD() && isAtTargetP();
    }
}
