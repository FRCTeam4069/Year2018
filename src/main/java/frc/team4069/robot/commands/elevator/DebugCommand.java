package frc.team4069.robot.commands.elevator;

import frc.team4069.robot.commands.CommandBase;

public class DebugCommand extends CommandBase {

    public DebugCommand(){
        requires(elevator);
    }

    @Override
    protected void initialize() {
        System.out.println("Elevator position: " + elevator.getPosition());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
