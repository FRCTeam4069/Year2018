package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathTeleopExchange extends SplinePath{
	
	public SplinePathTeleopExchange(){
		super(180, 218, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.222, 0.363));
		splinePoints.add(new DoublePoint(0.087, 2.403));
		splinePoints.add(new DoublePoint(0.446, 4.4));
		splinePoints.add(new DoublePoint(-0.054, 6.4));
		splinePoints.add(new DoublePoint(0.446, 7.7));
		super.setPoints(splinePoints);
	}
	
}