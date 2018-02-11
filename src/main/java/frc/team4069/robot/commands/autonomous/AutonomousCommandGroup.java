package frc.team4069.robot.commands.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4069.robot.commands.elevator.SetElevatorPositionCommand;
import frc.team4069.robot.commands.vacuum.StartVacuumCommand;
import frc.team4069.robot.subsystems.ElevatorSubsystem.Position;

// Command group that does everything involved in autonomous mode
public class AutonomousCommandGroup extends CommandGroup {

    // Turning angles and driving distances for each possible starting configuration
    private final double[] turningAngles = {10, -10, -30, 30, 10, -10};
    private final double[] drivingDistancesMeters = {4, 6, 9, 9, 6, 4};

    // The robot's starting position for autonomous mode
    // 0 is left, 1 is center, and 2 is right
    private int startingPosition = 2;

    // Constructor that runs all necessary commands in parallel
    public AutonomousCommandGroup() {
        // Get the angle to turn and the distance to travel before turning back the same angle
        int index = getTurningParametersIndex();
        double turningAngle = turningAngles[index];
        double drivingDistance = drivingDistancesMeters[index];
        // Run the commands in sequence
        addSequential(new StartVacuumCommand());
        addSequential(new WaitCommand(1));
        addSequential(new SetElevatorPositionCommand(Position.SWITCH));
        addSequential(new RotateToAngleWithGyroCommand(turningAngle));
        addSequential(new DriveStraightForDistanceCommand(drivingDistance));
        addSequential(new RotateToAngleWithGyroCommand(-turningAngle));
    }

    // Read the game data and get the direction to drive
    private int getTurningParametersIndex() {
        // Get the game data as a string
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        // Assume that the left switch is the one to go to
        boolean isRight = false;
        // If the length of the game data string is 3 as it should be
        if (gameData.length() == 3) {
            // Check if the first character is left or right
            isRight = gameData.charAt(0) == 'R';
        }
        // Get the index of the turning parameters by taking the starting position and adding 3 if
        // the right switch is being used
        return startingPosition + (isRight ? 3 : 0);
    }
}
