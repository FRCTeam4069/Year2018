package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathSwitchLeftOld extends SplinePath{
	
	public SplinePathSwitchLeftOld(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.068, 0.531));
		splinePoints.add(new DoublePoint(-0.621, 0.685));
		splinePoints.add(new DoublePoint(-1.278, 0.793));
		splinePoints.add(new DoublePoint(-1.544, 2.15/*2.533*/));
		super.points = splinePoints;
	}
	
}