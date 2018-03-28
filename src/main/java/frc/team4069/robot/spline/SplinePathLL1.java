package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathLL1 extends SplinePath{
	
	public SplinePathLL1(){
		super(270, 90, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(1.381, 1.427));
		splinePoints.add(new DoublePoint(2.352, 2.561));
		splinePoints.add(new DoublePoint(2.448, 4.17));
		splinePoints.add(new DoublePoint(1.909, 5.317));
		splinePoints.add(new DoublePoint(-0.401, 5.473));
		splinePoints.add(new DoublePoint(-0.993, 5.423));
		splinePoints.add(new DoublePoint(-1.194, 5.101));
		splinePoints.add(new DoublePoint(-1.214, 4.602));
		super.setPoints(splinePoints);
	}
	
}