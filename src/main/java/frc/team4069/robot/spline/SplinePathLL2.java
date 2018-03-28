package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLL2 extends SplinePath{
	
	public SplinePathLL2(){
		super(0, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.541, 0.202));
		splinePoints.add(new DoublePoint(-0.536, 1.452));
		super.setPoints(splinePoints);
	}
	
}