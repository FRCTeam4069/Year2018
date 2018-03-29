package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLL1 extends SplinePath{
	
	public SplinePathLL1(){
		super(270, 180, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(1.381, 1.427));
		splinePoints.add(new DoublePoint(2.352, 2.561));
		splinePoints.add(new DoublePoint(2.448, 4.17));
		splinePoints.add(new DoublePoint(1.909, 4.747));
		splinePoints.add(new DoublePoint(-2.3, 4.747));
		super.setPoints(splinePoints);
	}
	
}