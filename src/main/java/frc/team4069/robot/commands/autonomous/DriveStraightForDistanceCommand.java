package frc.team4069.robot.commands.autonomous;

import frc.team4069.robot.commands.CommandBase;

class DriveStraightForDistanceCommand extends CommandBase {

    private final double speed = 0.2;

    private double distanceMeters;
    private double endDistance;

    DriveStraightForDistanceCommand(double distanceMeters) {
        requires(driveBase);
        this.distanceMeters = distanceMeters;
    }

    @Override
    protected void initialize() {
        super.initialize();
        endDistance = driveBase.getDistanceTraveledMeters() + distanceMeters;
        driveBase.driveContinuousSpeed(0, speed);
    }

    @Override
    protected boolean isFinished() {
        return driveBase.getDistanceTraveledMeters() >= endDistance;
    }

    @Override
    protected void end() {
        driveBase.stop();
    }
}
