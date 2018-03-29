package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;
import frc.team4069.robot.subsystems.ElevatorSubsystem;
import frc.team4069.robot.commands.CommandBase;
import frc.team4069.robot.motors.PID;
import frc.team4069.robot.io.Input;

public class SetElevatorPositionSlowCommand extends CommandBase {
	
	private PID elevatorPID;
	
	private final int acceptableError = 1000;
	
	private int ticksBeforeFinished = 0;
	
	private double targetPosition;
	
	private double maxSpeedUp = 0.9;
	private double maxSpeedDown = 0.4;
	
	private boolean exitCommand = false;
	private boolean cancelCommand = false;
	
	private boolean movingUp;
	
	private boolean exitOnInput;
	private boolean cancelOnInput;
	
    public SetElevatorPositionSlowCommand(double position) {
		if(position > 0 || position < ElevatorSubsystem.MAX_POSITION_TICKS){
			System.out.println("Error: position is not in acceptable range");
			exitCommand = true;
		}
		targetPosition = position;
		requires(elevator);
    }
	
	public SetElevatorPositionSlowCommand(double position, boolean exitOnInput, boolean cancelOnInput) {
		if(position > 0 || position < ElevatorSubsystem.MAX_POSITION_TICKS){
			System.out.println("Error: position is not in acceptable range");
			exitCommand = true;
		}
		this.exitOnInput = exitOnInput;
		this.cancelOnInput = cancelOnInput;
		targetPosition = position;
		requires(elevator);
    }
	
	public SetElevatorPositionSlowCommand(double position, double upSpeed, boolean exitOnInput, boolean cancelOnInput) {
		if(position > 0 || position < ElevatorSubsystem.MAX_POSITION_TICKS){
			System.out.println("Error: position is not in acceptable range");
			exitCommand = true;
		}
		this.exitOnInput = exitOnInput;
		this.cancelOnInput = cancelOnInput;
		targetPosition = position;
		maxSpeedUp = upSpeed;
		requires(elevator);
    }
	
	public SetElevatorPositionSlowCommand(double position, double upSpeed) {
		this(position);
		maxSpeedUp = upSpeed;
    }
	
	public SetElevatorPositionSlowCommand(Position position){
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
			elevatorPID = new PID(0.0001, 0.00001, 0.0000005);
		}
		elevatorPID.setOutputCap(1.0);
		elevatorPID.setTarget(targetPosition);
    }
	
	@Override
	protected void execute(){
		double ticksTraveled = elevator.getPosition();
		if(Math.abs(Input.getElevatorAxis()) > 0.1){
			if(exitOnInput){
				exitCommand = true;
			}
			if(cancelOnInput){
				cancelCommand = true;
			}
		}
		if(ticksTraveled < targetPosition + acceptableError && ticksTraveled > targetPosition - acceptableError){
			ticksBeforeFinished++;
		}
		double speedFactor = lerp(0.25, 1, 0, -10000, ticksTraveled);
		if(speedFactor > 1){
			speedFactor = 1;
		}
		else if(speedFactor < 0.25){
			speedFactor = 0.25;
		}
		double motorOutput = elevatorPID.getMotorOutput(ticksTraveled);
		if(movingUp){
			motorOutput *= maxSpeedUp;
		}
		else{
			motorOutput *= maxSpeedDown;
		}
        elevator.setConstantSpeed(motorOutput * speedFactor);
	}

    @Override
    protected boolean isFinished() {
		if(cancelCommand){
			this.getGroup().cancel();
			return true;
		}
		if(exitCommand){
			return true;
		}
		if(ticksBeforeFinished >= 50){
			elevator.setConstantSpeed(0);
			return true;
		}
		return false;
    }
	
	private double lerp(double a, double b, double a2, double b2, double c) {
        double x = (c - a2) / (b2 - a2);
        return x * b + (1 - x) * a;
    }
	
}
