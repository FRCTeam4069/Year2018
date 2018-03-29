package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLL2 extends SplinePath{
	
	public SplinePathLL2(){
		super(180, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.555, 0.2));
		splinePoints.add(new DoublePoint(-0.614, 1.708));
		super.setPoints(splinePoints);
	}
	
}