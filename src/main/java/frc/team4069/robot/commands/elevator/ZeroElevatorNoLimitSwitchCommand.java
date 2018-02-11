package frc.team4069.robot.commands.elevator;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.io.IOMapping;

// Use our enum values with MotionMagic to move the elevator to predefined locations passed in the constructor
public class ZeroElevatorNoLimitSwitchCommand extends CommandBase {
	
	private PowerDistributionPanel pdp;
	
	// Has the elevator channel's amperage spiked
	private boolean elevatorZeroed = false;
	
	// Average elevator current calculated over command's runtime
	private double averageCurrent;
	
	// Number of times execute has been called
	private int iterations = 0;
	
	// Minimum percentage that current has to increase from averageCurrent to stop the elevator
	private int minimumPercentageCurrentIncrease = 100;
	
	// Time in milliseconds at which the command started
	private long startTime;
	
	// Time in milliseconds before calculation of average begins, to avoid being thrown off by initial spike
	private int delay = 1000;
	
    // Initializer in which this command requires the elevator subsystem
    public ZeroElevatorNoLimitSwitchCommand() {
        requires(elevator);
		pdp = getPowerDistributionPanel();
    }
	
	@Override
	protected void execute(){
		// Repeatedly check amperage in elevator channel and halt when it spikes
		if((int)(System.currentTimeMillis() - startTime) > delay){
			iterations++;
			double current = pdp.getCurrent(IOMapping.ELEVATOR_POWER_CHANNEL);
			if(iterations > 1 && ((current / averageCurrent) - 1) * 100 > minimumPercentageCurrentIncrease){
				elevatorZeroed = true;
				return;
			}
			double averageCurrentMultiplier = (iterations - 1) / iterations;
			averageCurrent = averageCurrent * averageCurrentMultiplier + current * (1 - averageCurrentMultiplier);
		}
	}
	
    @Override
    protected void initialize() {
		// Set start time
		startTime = System.currentTimeMillis();
        // Run the elevator downward slowly
        elevator.setSpeed(0.2);
    }

    @Override
    protected boolean isFinished() {
        // Run until the amperage in the elevator channel spikes
        return elevatorZeroed;
    }

    @Override
    protected void end() {
        // When the elevator amperage spikes, zero the elevator encoder
        elevator.reset();
    }
}