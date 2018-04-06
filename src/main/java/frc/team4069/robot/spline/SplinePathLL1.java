package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLL1 extends SplinePath{
	
	public SplinePathLL1(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(-0.0, 0.0));
		splinePoints.add(new DoublePoint(-1.157, 1.053));
		splinePoints.add(new DoublePoint(-1.578, 2.5));
		super.setPoints(splinePoints, true);
	}
	
}