package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// Using the gyroscope, rotate the robot to a specific angle
class RotateToAngleWithGyroCommand extends CommandBase {

    private final double turnSpeedAbsolute = 0.6;
    private boolean turnRight;
    private double endAngle;

    RotateToAngleWithGyroCommand(double endAngle) {
        requires(driveBase);
        this.endAngle = endAngle;
    }

    protected void initialize() {
        // Turn right if the distance from the end angle to the current angle is between 0 and 180
        turnRight = (endAngle - getGyroAngle()) < 180;
        // Turn on the spot in the calculated direction
        driveBase.rotate(turnRight ? turnSpeedAbsolute : -turnSpeedAbsolute);
    }

    protected boolean isFinished() {
        if (turnRight) {
            return getGyroAngle() >= endAngle;
        } else {
            return getGyroAngle() <= endAngle;
        }
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
