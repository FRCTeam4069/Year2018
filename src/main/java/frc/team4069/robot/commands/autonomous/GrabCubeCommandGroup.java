package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Series of actions to unlatch the vacuum, pick up the cube, and bring it to switch height
class GrabCubeCommandGroup extends CommandGroup {

    GrabCubeCommandGroup() {
        addSequential(new StartVacuumCommand());
        addSequential(new SetElevatorPositionCommand(-6000));
        addSequential(new SetElevatorPositionCommand(Position.MINIMUM));
        addSequential(new SetElevatorPositionCommand(Position.SWITCH));
    }
}
