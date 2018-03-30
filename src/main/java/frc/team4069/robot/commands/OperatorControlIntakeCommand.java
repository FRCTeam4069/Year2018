package frc.team4069.robot.commands;

import frc.team4069.robot.io.Input;

public class OperatorControlIntakeCommand extends CommandBase {
	
    public OperatorControlIntakeCommand() {
        requires(vacuum);
    }

    @Override
    protected void initialize() {
        
    }

    @Override
    protected void execute() {
		vacuum.setConstantSpeed(Input.getIntakeSpeed());
    }

    private double lerp(double a, double b, double a2, double b2, double c) {
        double x = (c - a2) / (b2 - a2);
        return x * b + (1 - x) * a;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
