package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathSwitchRight extends SplinePath{
	
	public SplinePathSwitchRight(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0.29, 0.621));
		splinePoints.add(new DoublePoint(0.856, 0.823));
		splinePoints.add(new DoublePoint(1.152, 2.15/*2.561*/));
		super.points = splinePoints;
	}
	
}