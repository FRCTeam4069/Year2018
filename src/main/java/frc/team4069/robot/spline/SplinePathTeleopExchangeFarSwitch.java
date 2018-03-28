package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathTeleopExchangeFarSwitch extends SplinePath{
	
	public SplinePathTeleopExchangeFarSwitch(){
		super(180, 322, 250);
		ArrayList<DoublePoint> splinePoints = new ArrayList<DoublePoint>();
		splinePoints.add(new DoublePoint(0.0, 0.0));
		splinePoints.add(new DoublePoint(-0.341, 4.71));
		splinePoints.add(new DoublePoint(-2.002, 6.46));
		splinePoints.add(new DoublePoint(-5.013, 6.713));
		splinePoints.add(new DoublePoint(-6.66, 7.647));
		super.setPoints(splinePoints);
	}
	
}