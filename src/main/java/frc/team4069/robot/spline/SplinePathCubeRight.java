package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathCubeRight extends SplinePath{
	
	public SplinePathCubeRight(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.82, 0.253));
		splinePoints.add(new DoublePoint(-1.42, 1.213));
		super.points = splinePoints;
	}
	
}