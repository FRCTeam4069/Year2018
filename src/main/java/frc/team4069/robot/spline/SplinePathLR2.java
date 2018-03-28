package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLR2 extends SplinePath{
	
	public SplinePathLR2(){
		super(150, 90, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(1.057, -0.658));
		splinePoints.add(new DoublePoint(2.79, 0.628));
		splinePoints.add(new DoublePoint(2.733, 3.456));
		splinePoints.add(new DoublePoint(2.595, 4.19));
		super.setPoints(splinePoints);
	}
	
}