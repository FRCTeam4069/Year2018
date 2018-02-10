package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
        // Start the vacuum and wait 2 seconds for the vacuum to reach full speed
        addSequential(new StartVacuumCommand());
        addSequential(new WaitCommand(2));
        // Bring the elevator up to driving height and drive towards the switch in parallel
        addParallel(new SetElevatorPositionCommand(Position.SWITCH));
        addParallel(new DriveToSwitchCommand());
    }
}
