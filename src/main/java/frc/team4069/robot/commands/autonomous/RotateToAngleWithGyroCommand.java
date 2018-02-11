package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// Using the gyroscope, rotate the robot to a specific angle
class RotateToAngleWithGyroCommand extends CommandBase {

    private final double turnSpeedAbsolute = 0.2;
    private boolean turnRight;
    private double relativeAngle;
    private double endAngle;

    RotateToAngleWithGyroCommand(double relativeAngle) {
        requires(driveBase);
        this.relativeAngle = relativeAngle;
    }

    protected void initialize() {
        double currentAngle = getGyroAngle();
        endAngle = currentAngle + relativeAngle;
        // Turn right if the distance from the end angle to the current angle is between 0 and 180
        turnRight = (endAngle - currentAngle) < 180;
        // Turn on the spot in the calculated direction
        driveBase.rotate(turnRight ? turnSpeedAbsolute : -turnSpeedAbsolute);
        System.out.println(turnRight ? turnSpeedAbsolute : -turnSpeedAbsolute);
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
