package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathTeleopExchangeFarScale extends SplinePath{
	
	public SplinePathTeleopExchangeFarScale(){
		super(180, 322, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.385, 1.521));
		splinePoints.add(new DoublePoint(-1.157, 2.322));
		splinePoints.add(new DoublePoint(-3.515, 2.307));
		splinePoints.add(new DoublePoint(-5.933, 2.604));
		splinePoints.add(new DoublePoint(-5.77, 6.698));
		splinePoints.add(new DoublePoint(-6.66, 7.647));
		super.setPoints(splinePoints, true);
	}
	
}