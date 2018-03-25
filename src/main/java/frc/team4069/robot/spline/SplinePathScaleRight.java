package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathScaleRight extends SplinePath{
	
	public SplinePathScaleRight(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(0, 2.081));
		splinePoints.add(new DoublePoint(-0.015, 2.691));
		splinePoints.add(new DoublePoint(-0.116, 3.516));
		splinePoints.add(new DoublePoint(-0.218, 4.342));
		splinePoints.add(new DoublePoint(-0.414, 5.18));
		splinePoints.add(new DoublePoint(-0.903, 6.018));
		super.points = splinePoints;
	}
	
}