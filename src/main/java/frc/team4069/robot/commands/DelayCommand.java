package frc.team4069.robot.commands;

import frc.team4069.robot.commands.CommandBase;

public class DelayCommand extends CommandBase {

	private int milliseconds;
	
	private long startTime;

    public DelayCommand(int milliseconds) {
        this.milliseconds = milliseconds;
    }
	
	@Override
	protected void initialize(){
		startTime = System.currentTimeMillis();
	}
	
	@Override
	protected boolean isFinished(){
		return (int)(System.currentTimeMillis() - startTime) >= milliseconds;
	}
	
}
