package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathCircle extends SplinePath{
	
	public SplinePathCircle(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-1.0, 1.0));
		splinePoints.add(new DoublePoint(-2.0, 0.0));
		splinePoints.add(new DoublePoint(-1.0, -1.0));
		splinePoints.add(new DoublePoint(0.0, 0.0));
		super.setPoints(splinePoints);
	}
	
}