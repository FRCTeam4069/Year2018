package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLR1 extends SplinePath{
	
	public SplinePathLR1(){
		super(270, 0, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-1.681, 1.427));
		splinePoints.add(new DoublePoint(-2.652, 2.561));
		splinePoints.add(new DoublePoint(-2.748, 4.17));
		splinePoints.add(new DoublePoint(-2.209, 5.047));
		splinePoints.add(new DoublePoint(-0.418, 5.047));
		super.setPoints(splinePoints);
	}
	
}