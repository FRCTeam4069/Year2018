package frc.team4069.robot.spline;

import java.awt.Point;

public class Vector {
	
	public double x, y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector(Point p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Vector(DoublePoint p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public Vector add(Vector other){
		return new Vector(x + other.x, y + other.y);
	}
	
	public Vector sub(Vector other){
		return new Vector(x - other.x, y - other.y);
	}
	
	public Vector multScalar(double scalar){
		return new Vector(x * scalar, y * scalar);
	}
	
	public Vector normalize(){
		double len = length();
		return new Vector(x / len, y / len);
	}
	
	public double dot(Vector other){
		return x * other.x + y * other.y;
	}
	
	public double length(){
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
}
