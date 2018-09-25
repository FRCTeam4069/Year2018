package frc.team4069.robot.spline;

public class CubicEquation {
	
	public double ax, bx, cx, dx, ay, by, cy, dy;
	
	public CubicEquation(double ax, double bx, double cx, double dx, double ay, double by, double cy, double dy){
		this.ax = ax;
		this.bx = bx;
		this.cx = cx;
		this.dx = dx;
		this.ay = ay;
		this.by = by;
		this.cy = cy;
		this.dy = dy;
	}
	
	public CubicEquation(){
		ax = bx = cx = dx = ay = by = cy = dy = 0;
	}
	
	public DoublePoint evaluate(double t){
		return new DoublePoint(ax + bx * t + cx * Math.pow(t, 2) + dx * Math.pow(t, 3), ay + by * t + cy * Math.pow(t, 2) + dy * Math.pow(t, 3));
	}
	
}
