package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

public class DriveStraightForDistanceCommand extends CommandBase {

    private final double speed = 0.2;

    private double distanceMeters;
    private double endDistance;
    private double signedSpeed;

    public DriveStraightForDistanceCommand(double distanceMeters) {
        requires(driveBase);
        this.distanceMeters = distanceMeters;
        signedSpeed = distanceMeters > 0 ? speed : -speed;
    }

    @Override
    protected void initialize() {
        super.initialize();
        endDistance = driveBase.getDistanceTraveledMeters() + distanceMeters;
        driveBase.driveContinuousSpeed(0, signedSpeed, true);
    }

    @Override
    protected boolean isFinished() {
        double distance = Math.abs(driveBase.getDistanceTraveledMeters());
        System.out.println("Distance is " + distance);
        System.out.println("End distance is " + endDistance);
//        return driveBase.getDistanceTraveledMeters() >= endDistance;
        return distance >= endDistance;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
