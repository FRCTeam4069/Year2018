package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;
import frc.team4069.robot.subsystems.ElevatorSubsystem;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.motors.PID;

public class SetElevatorPositionCommand extends CommandBase {
	
	private PID elevatorPID;
	
	private final int acceptableError = 1000;
	
	private int ticksBeforeFinished = 0;
	
	private double targetPosition;
	
	private double maxSpeedUp = 0.8;
	private double maxSpeedDown = 0.4;
	
	private boolean exitCommand = false;
	
	private boolean movingUp;
	
    public SetElevatorPositionCommand(double position) {
		if(position > 0 || position < ElevatorSubsystem.MAX_POSITION_TICKS){
			System.out.println("Error: position is not in acceptable range");
			exitCommand = true;
		}
		targetPosition = position;
		requires(elevator);
    }
	
	public SetElevatorPositionCommand(Position position){
		this(position.getTicks());
	}

    @Override
    protected void initialize() {
        double currentPosition = elevator.getPosition();
		if(targetPosition < currentPosition){
			movingUp = true;
		}
		else{
			movingUp = false;
		}
        
		if(movingUp){
			elevatorPID = new PID(0.0002, 0.00001, 0.0000005);
		}
		else{
			elevatorPID = new PID(0.0002, 0.00001, 0.0000005);
		}
		elevatorPID.setOutputCap(1.0);
		elevatorPID.setTarget(targetPosition);
    }
	
	@Override
	protected void execute(){
		double ticksTraveled = elevator.getPosition();
		if(ticksTraveled < targetPosition + acceptableError && ticksTraveled > targetPosition - acceptableError){
			ticksBeforeFinished++;
		}
		double motorOutput = elevatorPID.getMotorOutput(ticksTraveled);
		if(movingUp){
			motorOutput *= maxSpeedUp;
		}
		else{
			motorOutput *= maxSpeedDown;
		}
        elevator.setConstantSpeed(motorOutput);
	}

    @Override
    protected boolean isFinished() {
		if(exitCommand){
			return true;
		}
		if(ticksBeforeFinished >= 10){
			elevator.setConstantSpeed(0);
			return true;
		}
		return false;
    }
	
}