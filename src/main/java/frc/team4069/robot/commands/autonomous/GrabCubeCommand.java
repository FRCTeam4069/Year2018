package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Series of actions to unlatch the vacuum, pick up the cube, and bring it to switch height
public class GrabCubeCommand extends CommandGroup {

    public GrabCubeCommand() {
        addSequential(new SetElevatorPositionCommand(Position.SWITCH));
        addSequential(new WaitCommand(2));
        addSequential(new StartVacuumCommand());
        addSequential(new SetElevatorPositionCommand(Position.MINIMUM));
        addSequential(new WaitCommand(2.5));
        addSequential(new SetElevatorPositionCommand(Position.SWITCH));
    }
}
