package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

public class DriveStraightForDistanceCommand extends CommandBase {

    private double distanceMeters;
    private double signedSpeed;
    private double initialPosition;

    public DriveStraightForDistanceCommand(double distanceMeters, double speed) {
        requires(driveBase);
        this.distanceMeters = Math.abs(distanceMeters);
        signedSpeed = distanceMeters > 0 ? speed : -speed;
    }

    @Override
    protected void initialize() {
        super.initialize();
        initialPosition = driveBase.getDistanceTraveledMeters();
        driveBase.driveContinuousSpeed(0, signedSpeed, true);
    }

    @Override
    protected boolean isFinished() {
        double distance = Math.abs(initialPosition - driveBase.getDistanceTraveledMeters());
        return distance >= distanceMeters;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
