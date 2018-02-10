package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
        // Drive forward and turn towards the switch
        addSequential(new DriveToSwitchCommand());
    }
}
