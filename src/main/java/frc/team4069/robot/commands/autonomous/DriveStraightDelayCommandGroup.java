package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.DelayCommand;

public class DriveStraightDelayCommandGroup extends CommandGroup {

    public DriveStraightDelayCommandGroup(int delay, double meters) {
        addSequential(new DelayCommand(delay));
        addSequential(new DriveStraightForDistanceCommand(meters, 0.6));
    }

}
