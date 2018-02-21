package frc.team4069.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// A command group that starts the vacuum, aligns with the power cube using vision,
// drops the elevator to grab the cube, and then raises it back to exchange height
public class ElevatorIntakeCommandGroup extends CommandGroup {

    // Constructor that runs the four commands sequentially
    public ElevatorIntakeCommandGroup() {
        addSequential(new StartVacuumCommand());
        addSequential(new SetElevatorPositionCommand(Position.MINIMUM, true));
//        addSequential(new WaitCommand(0.2));
        addSequential(new SetElevatorPositionCommand(Position.EXCHANGE, false));
    }
}
