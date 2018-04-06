package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathScaleLeft extends SplinePath{
	
	public SplinePathScaleLeft(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(1.4, 1.453));
		splinePoints.add(new DoublePoint(2.3, 1.953));
		splinePoints.add(new DoublePoint(2.5, 3.453));
		splinePoints.add(new DoublePoint(2.5, 4.753));
		splinePoints.add(new DoublePoint(0.66, 5.001));
		splinePoints.add(new DoublePoint(-2.1, 5.153));
		splinePoints.add(new DoublePoint(-2.053, 6.452));
		super.setPoints(splinePoints, true);
	}
	
}