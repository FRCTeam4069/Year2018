package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// Using the gyroscope, rotate the robot to a specific angle
class RotateToAngleWithGyroCommand extends CommandBase {

    private final double turnSpeedAbsolute = 0.2;
    private boolean turnRight;
    private double startAngle;
    private double relativeAngle;

    //Note: relativeAngle can be negative or positive angle
    RotateToAngleWithGyroCommand(double relativeAngle) {
        requires(driveBase);
        this.relativeAngle = relativeAngle;
		if(this.relativeAngle > 180){
			this.relativeAngle -= 360;
		}
    }


    protected void initialize() {
        startAngle = getGyroAngle();
        // If passed angle to turn is positive, turn right
        turnRight = relativeAngle > 0;
        // Turn on the spot in the calculated direction
        driveBase.rotate(turnRight ? turnSpeedAbsolute : -turnSpeedAbsolute);
    }

    protected boolean isFinished() {
        System.out.println("get: " + getGyroAngle());
        System.out.println("turnright:" + turnRight);
		double gyroAngle = getGyroAngle();
		if(gyroAngle < startAngle && this.turnRight){
			gyroAngle += 360;
		}
		else if(gyroAngle > startAngle && !this.turnRight){
			gyroAngle -= 360;
		}
        double delta = gyroAngle - startAngle;
        if (turnRight) {
            return delta >= relativeAngle;
        } else {
            return delta <= relativeAngle;
        }
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
