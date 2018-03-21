package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathScaleLeft extends SplinePath{
	
	public SplinePathScaleLeft(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0, 2.081));
		splinePoints.add(new DoublePoint(0, 4.126));
		splinePoints.add(new DoublePoint(-1.018, 4.875));
		splinePoints.add(new DoublePoint(-3.977, 4.876));
		splinePoints.add(new DoublePoint(-4.98, 5.396));
		splinePoints.add(new DoublePoint(-5.056, 6.298));
		super.points = splinePoints;
	}
	
}