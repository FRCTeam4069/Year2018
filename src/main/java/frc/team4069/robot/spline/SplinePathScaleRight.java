package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathScaleRight extends SplinePath{
	
	public SplinePathScaleRight(){
		super(270, 300, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(1.6, 1.453));
		splinePoints.add(new DoublePoint(2.5, 1.953));
		splinePoints.add(new DoublePoint(2.6, 4.253));
		splinePoints.add(new DoublePoint(2.3, 6.453));
		super.points = splinePoints;
	}
	
}