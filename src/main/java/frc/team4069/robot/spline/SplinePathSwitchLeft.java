package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathSwitchLeft extends SplinePath{
	
	public SplinePathSwitchLeft(){
		super(270, 270, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.301, 0.726));
		splinePoints.add(new DoublePoint(-0.972, 0.817));
		splinePoints.add(new DoublePoint(-1.413, 1.284));
		splinePoints.add(new DoublePoint(-1.528, 2.071));
		super.setPoints(splinePoints);
	}
	
}