package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathCubeLeft extends SplinePath{
	
	public SplinePathCubeLeft(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0.785, 0.653));
		splinePoints.add(new DoublePoint(1.385, 1.513));
		super.setPoints(splinePoints);
	}
	
}