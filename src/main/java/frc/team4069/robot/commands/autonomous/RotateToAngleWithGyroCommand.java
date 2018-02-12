package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

// Using the gyroscope, rotate the robot to a specific angle
class RotateToAngleWithGyroCommand extends CommandBase {

    private final double turnSpeedAbsolute = 0.2;
    private boolean turnRight;
    private double relativeAngle;
    private double endAngle;

    //Note: relativeAngle can be negative or positive angle
    RotateToAngleWithGyroCommand(double relativeAngle) 
    {
        requires(driveBase);
        this.relativeAngle = relativeAngle % 180;
    }


    
    
    protected void initialize() 
    {
        double currentAngle = getGyroAngle();
        endAngle = (currentAngle + relativeAngle) % 360; //javas % is not modulus, its remainder so it can be negative
        												//true modulus can never be negative
        if (endAngle < 0) endAngle += 360;				//so if it goes negative, blow it positive
        
        turnRight = relativeAngle > 0 ? true : false;  //if passed angle to turn is positive, turn right 
        
        // Turn right if the distance from the end angle to the current angle is between 0 and 180
        //turnRight = (endAngle - currentAngle) < 180;
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
