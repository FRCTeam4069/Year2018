package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathSwitchRight extends SplinePath{
	
	public SplinePathSwitchRight(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0.857, 1.053));
		splinePoints.add(new DoublePoint(1.278, 2.5));
		super.points = splinePoints;
	}
	
}