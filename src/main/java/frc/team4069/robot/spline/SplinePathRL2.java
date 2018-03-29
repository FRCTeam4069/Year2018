package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathRL2 extends SplinePath{
	
	public SplinePathRL2(){
		super(0, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-1.495, 0.0));
		splinePoints.add(new DoublePoint(-2.255, 0.2));
		splinePoints.add(new DoublePoint(-2.314, 1.708));
		super.setPoints(splinePoints);
	}
	
}