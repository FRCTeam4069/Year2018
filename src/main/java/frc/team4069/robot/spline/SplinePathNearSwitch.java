package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathNearSwitch extends SplinePath{
	
	public SplinePathNearSwitch(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0.5, 1.0));
		splinePoints.add(new DoublePoint(1.0, 2.0));
		super.points = splinePoints;
	}
	
}