package frc.team4069.robot.util;

import java.util.Date;

/**
 * An easy to use low pass filter. Passed a time in milliseconds representing the cutoff frequency of the filter. So any events which happen quicker than the passed millisecond value will be dampened down, events which happen exactly as often as the passed RC value will take 50%
 * of the calculated passed value and 50% of the previous value to arrive at the new value.
 */
public class LowPassFilter {

    private double m_lastValue = 0;

    private long m_lastTime = -1;

    private double m_RC; // Time constant (tau) of the filter
    // if set near zero little filtering happens
    // DO NOT SET TO ZERO

    /**
     * Specifies an RC value and construct a new LowPassFilter object.
     */
    public LowPassFilter(double RC) {
        m_RC = RC;
    }

    /**
     * Resets the LowPassFilter.
     */
    public void reset() {
        m_lastValue = 0;
        m_lastTime = -1;
    }

    /**
     * Retrieves an RC value.
     */
    public double getRC() {
        return m_RC;
    }

    /**
     * Sets an RC value
     */
    public void setRC(double RC) {
        m_RC = RC;
    }

    /**
     * Call this everytime you need to calculate the value. Recommend you do this every iteration. Otherwise try to reset it.
     *
     * @param value The value to go in
     * @return The value the filter computes
     *
     * if the time between calls to calculate are faster than the RC value the value passed to calculate will be scaled down in importance, if the time between calls to calculate is greater than RC, the value passed to calculate will have greater significance in the return
     * value.
     *
     * if the time between calls to calculate exactly equals RC, than 50% of the value passed to calculate will be combined with 50% of the previously stored value and that will be returned.
     */
    public double calculate(double value) {
        if (m_lastTime > 0) // If NOT the first time calculate has been called....
        {
            long currentTime = new Date().getTime();
            double a = currentTime - m_lastTime; // Get milliseconds passed since last time called.

            a /= (a + m_RC); // get value between 0 and 1, fraction represents milliseconds passed
            // scaled by m_RC value, this could crash if m_RC==0 and < 1 millisecond has passed!
            // potential 0/0 error div zero
            // A tends towards 1.0 as more time has passed, very quick calls make a tend towards 0.0

            m_lastTime = currentTime;

            /*
             * Now we want to scale the passed in value by how important it is. The more frequently calculate is called, the LESS important the value is, the less frequently calculate is called the MORE important value is.
             *
             * a * value will scale the input value down by a lot if not a lot of time has passed between calls to calculate 1-a will be near 1.0 if calculate is called very quickly, and near 0.0 for very long delays in calling calculate (1-a) is basically the inverse of a, when a is
             * near 1.0, 1-a will be near 0
             */
            m_lastValue = a * value + (1 - a)
                    * m_lastValue; // scale new value's importance by how frequently calculate is being called
            // then add in the old last value, scaled also by the invsere of how frequently called.
            // so when called often, the old value is more important, and the new value is more ignored
            // when called less often, the old value is less important and the new value is given more importance
        } else {
            m_lastTime = new Date()
                    .getTime(); // This is the first time calculate has been called since a reset, fill in time value and return
            // default m_lastValue (0) from constructor above
        }

        return m_lastValue;
    }
}
