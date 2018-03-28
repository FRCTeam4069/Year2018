package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathTeleopScale extends SplinePath{
	
	public SplinePathTeleopScale(){
		super(90, 180, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-1.043, -0.479));
		super.setPoints(splinePoints);
	}
	
}