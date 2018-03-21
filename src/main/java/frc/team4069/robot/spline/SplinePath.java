package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePath{
	
	public ArrayList<DoublePoint> points;
	
	public int startAngle, endAngle, angleWeight;
	
	public SplinePath(ArrayList<DoublePoint> points, int startAngle, int endAngle, int angleWeight){
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.angleWeight = angleWeight;
		this.points = points;
	}
	
	public SplinePath(int startAngle, int endAngle, int angleWeight){
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.angleWeight = angleWeight;
	}
	
	public void scale(double scalar){
		for(int i = 0; i < points.size(); i++){
			points.get(i).x *= scalar;
			points.get(i).y *= scalar;
		}
	}
	
}