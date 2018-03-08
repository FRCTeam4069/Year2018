package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePathGenerator {
	
	private double cumulativeLeftWheelDistance = 0;
	private double cumulativeRightWheelDistance = 0;
	
	public DoublePoint[] pointsOnCurve;
	public DoublePoint[] leftWheel;
	public DoublePoint[] rightWheel;
	
	public double[] splineIntegral;
	public double[] leftWheelIntegral;
	public double[] rightWheelIntegral;
	
	public double[] splineAngles;
	public double splineStartAngle;
	
	private double driveBaseWidth;
	
	private int numPointsOnCurve;
	
	private CubicSplineInterpolator spline;
	
	public SplinePathGenerator(double driveBaseWidth){
		this.driveBaseWidth = driveBaseWidth;
	}
	
	public int getNumPointsOnCurve(){
		return numPointsOnCurve;
	}
	
	public double getDriveBaseWidth(){
		return driveBaseWidth;
	}
	
	public double getCumulativeLeftWheelDistance(){
		return cumulativeLeftWheelDistance;
	}
	
	public double getCumulativeRightWheelDistance(){
		return cumulativeRightWheelDistance;
	}
	
	public void generateSpline(ArrayList<DoublePoint> points, int startAngle, int endAngle, int weight){
		double totalPathSize = 0;
		for(int i = 1; i < points.size(); i++){
			totalPathSize += new Vector(points.get(i).x, points.get(i).y).sub(new Vector(points.get(i-1).x, points.get(i-1).y)).length();
		}
		numPointsOnCurve = (int)(400 * totalPathSize);
		spline = new CubicSplineInterpolator(points, startAngle, endAngle, weight);
		pointsOnCurve = new DoublePoint[numPointsOnCurve];
		leftWheel = new DoublePoint[numPointsOnCurve - 1];
		rightWheel = new DoublePoint[numPointsOnCurve - 1];
		splineAngles = new double[numPointsOnCurve - 1];
		splineIntegral = new double[numPointsOnCurve];
		leftWheelIntegral = new double[numPointsOnCurve - 1];
		rightWheelIntegral = new double[numPointsOnCurve - 1];
		DoublePoint prevPoint = null;
		cumulativeRightWheelDistance = cumulativeLeftWheelDistance = 0;
		for(int i = 0; i < numPointsOnCurve; i++){
			double t = i / (double)numPointsOnCurve;
			DoublePoint point = spline.getSplinePosition(t);
			pointsOnCurve[i] = new DoublePoint(point.x, point.y);
			if(i != 0){
				Vector pointVector = new Vector(point.x, point.y);
				Vector diff = pointVector.sub(new Vector(prevPoint.x, prevPoint.y));
				double angle = Math.toDegrees(Math.atan2(diff.y, diff.x));
				if(i == 1){
					splineStartAngle = angle;
				}
				splineAngles[i - 1] = angle - splineStartAngle;
				splineIntegral[i] = splineIntegral[i - 1] + diff.length();
				Vector normal = null;
				if(diff.x > 0 && diff.y > 0){
					normal = new Vector(-Math.sqrt(Math.pow(diff.y, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))), Math.sqrt(Math.pow(diff.x, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))));
				}
				else if(diff.x < 0 && diff.y > 0){
					normal = new Vector(-Math.sqrt(Math.pow(diff.y, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))), -Math.sqrt(Math.pow(diff.x, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))));
				}
				else if(diff.x > 0 && diff.y < 0){
					normal = new Vector(Math.sqrt(Math.pow(diff.y, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))), Math.sqrt(Math.pow(diff.x, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))));
				}
				else{
					normal = new Vector(Math.sqrt(Math.pow(diff.y, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))), -Math.sqrt(Math.pow(diff.x, 2) / (Math.pow(diff.x, 2) + Math.pow(diff.y, 2))));
				}
				Vector leftWheelPos = pointVector.add(normal.multScalar(driveBaseWidth / 2));
				leftWheel[i - 1] = new DoublePoint(leftWheelPos.x, leftWheelPos.y);
				Vector rightWheelPos = pointVector.add(normal.multScalar(-driveBaseWidth / 2));
				rightWheel[i - 1] = new DoublePoint(rightWheelPos.x, rightWheelPos.y);
				if(i != 1){
					Vector left = new Vector(leftWheel[i - 1]).sub(new Vector(leftWheel[i - 2]));
					if(left.length() > 0){
						double leftLength = left.length() * (left.normalize().dot(diff.normalize()) > 0 ? 1 : -1);
						leftWheelIntegral[i - 1] = leftWheelIntegral[i - 2] + leftLength;
						cumulativeLeftWheelDistance += leftLength;
					}
					Vector right = new Vector(rightWheel[i - 1]).sub(new Vector(rightWheel[i - 2]));
					if(right.length() > 0){
						double rightLength = right.length() * (right.normalize().dot(diff.normalize()) > 0 ? 1 : -1);
						rightWheelIntegral[i - 1] = rightWheelIntegral[i - 2] + rightLength;
						cumulativeRightWheelDistance += rightLength;
					}
				}
			}
			prevPoint = point;
		}
		leftWheelIntegral[leftWheelIntegral.length - 1] = leftWheelIntegral[leftWheelIntegral.length - 2];
		rightWheelIntegral[rightWheelIntegral.length - 1] = rightWheelIntegral[rightWheelIntegral.length - 2];
	}
	
}
