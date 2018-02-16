package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

public class DriveStraightForDistanceCommand extends CommandBase {

    private final double speed = 0.2;

    private double distanceMeters;
    private double signedSpeed;

    public DriveStraightForDistanceCommand(double distanceMeters) {
        requires(driveBase);
        this.distanceMeters = distanceMeters;
        signedSpeed = distanceMeters > 0 ? speed : -speed;
    }

    @Override
    protected void initialize() {
        super.initialize();
        driveBase.reset();
        driveBase.driveContinuousSpeed(0, signedSpeed, true);
    }

    @Override
    protected boolean isFinished() {
        double distance = Math.abs(driveBase.getDistanceTraveledMeters());
        return distance >= distanceMeters;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
