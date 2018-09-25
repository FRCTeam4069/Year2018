package frc.team4069.robot.spline;

import java.awt.Point;
import java.util.ArrayList;

public class CubicSplineInterpolator {
	
	private ArrayList<DoublePoint> points;
	
	private CubicEquation[] equations;
	
	private long startTime;
	
	private final int POINT_SCALING_FACTOR = 100;
	
	public CubicSplineInterpolator(ArrayList<DoublePoint> pts, int startAngle, int endAngle, int directionWeight){
		startTime = System.currentTimeMillis();
		this.points = clone(pts);
		for(DoublePoint p: this.points){
			p.x *= POINT_SCALING_FACTOR;
			p.y *= POINT_SCALING_FACTOR;
		}
		equations = new CubicEquation[points.size() - 1];
		startAngle = ((startAngle % 360) + 360) % 360;
		endAngle = ((endAngle % 360) + 360) % 360;
		double sa = Math.toRadians(startAngle);
		double ea = Math.toRadians(endAngle);
		int numUnknowns = (points.size() - 1) * 8;
		Matrix coeffs = new Matrix(numUnknowns, numUnknowns);
		Matrix values = new Matrix(1, numUnknowns);
		int counter = 0;
		for(int i = 0; i < points.size() - 1; i++){
			coeffs.values[i * 4][i + counter] = new Fraction(1);
			values.values[0][i + counter] = new Fraction((int)(points.get(i).x * 100000), 100000);
		}
		counter += points.size() - 1;
		for(int i = 0; i < points.size() - 1; i++){
			coeffs.values[i * 4 + numUnknowns / 2][i + counter] = new Fraction(1);
			values.values[0][i + counter] = new Fraction((int)(points.get(i).y * 100000), 100000);
		}
		counter += points.size() - 1;
		for(int i = 0; i < points.size() - 1; i++){
			coeffs.values[i * 4][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + 1][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + 2][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + 3][i + counter] = new Fraction(1);
			values.values[0][i + counter] = new Fraction((int)(points.get(i+1).x * 100000), 100000);
		}
		counter += points.size() - 1;
		for(int i = 0; i < points.size() - 1; i++){
			coeffs.values[i * 4 + numUnknowns / 2][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + numUnknowns / 2 + 1][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + numUnknowns / 2 + 2][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + numUnknowns / 2 + 3][i + counter] = new Fraction(1);
			values.values[0][i + counter] = new Fraction((int)(points.get(i+1).y * 100000), 100000);
		}
		counter += points.size() - 1;
		for(int i = 0; i < points.size() - 2; i++){
			coeffs.values[i * 4 + 1][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + 2][i + counter] = new Fraction(2);
			coeffs.values[i * 4 + 3][i + counter] = new Fraction(3);
			coeffs.values[i * 4 + 5][i + counter] = new Fraction(-1);
			values.values[0][i + counter] = new Fraction();
		}
		counter += points.size() - 2;
		for(int i = 0; i < points.size() - 2; i++){
			coeffs.values[i * 4 + numUnknowns / 2 + 1][i + counter] = new Fraction(1);
			coeffs.values[i * 4 + numUnknowns / 2 + 2][i + counter] = new Fraction(2);
			coeffs.values[i * 4 + numUnknowns / 2 + 3][i + counter] = new Fraction(3);
			coeffs.values[i * 4 + numUnknowns / 2 + 5][i + counter] = new Fraction(-1);
			values.values[0][i + counter] = new Fraction();
		}
		counter += points.size() - 2;
		for(int i = 0; i < points.size() - 2; i++){
			coeffs.values[i * 4 + 2][i + counter] = new Fraction(2);
			coeffs.values[i * 4 + 3][i + counter] = new Fraction(6);
			coeffs.values[i * 4 + 6][i + counter] = new Fraction(-2);
			values.values[0][i + counter] = new Fraction();
		}
		counter += points.size() - 2;
		for(int i = 0; i < points.size() - 2; i++){
			coeffs.values[i * 4 + numUnknowns / 2 + 2][i + counter] = new Fraction(2);
			coeffs.values[i * 4 + numUnknowns / 2 + 3][i + counter] = new Fraction(6);
			coeffs.values[i * 4 + numUnknowns / 2 + 6][i + counter] = new Fraction(-2);
			values.values[0][i + counter] = new Fraction();
		}
		counter += points.size() - 2;
		double by1 = 0, bx1 = 0, bxn = 0, byn = 0;
		if(startAngle % 90 == 0){
			int anglePhase = (int)(startAngle / 90) % 4;
			if(anglePhase == 0){
				bx1 = directionWeight;
				by1 = 0;
			}
			else if(anglePhase == 1){
				bx1 = 0;
				by1 = -directionWeight;
			}
			else if(anglePhase == 2){
				bx1 = -directionWeight;
				by1 = 0;
			}
			else if(anglePhase == 3){
				bx1 = 0;
				by1 = directionWeight;
			}
		}
		else{
			double tanSquaredSa = Math.pow(Math.tan(sa), 2);
			by1 = Math.sqrt((Math.pow(directionWeight, 2) * tanSquaredSa) / (1 + tanSquaredSa));
			bx1 = by1 / Math.tan(sa);
		}
		if(endAngle % 90 == 0){
			int anglePhase = (int)(endAngle / 90) % 4;
			if(anglePhase == 0){
				bxn = directionWeight;
				byn = 0;
			}
			else if(anglePhase == 1){
				bxn = 0;
				byn = -directionWeight;
			}
			else if(anglePhase == 2){
				bxn = -directionWeight;
				byn = 0;
			}
			else if(anglePhase == 3){
				bxn = 0;
				byn = directionWeight;
			}
		}
		else{
			double tanSquaredEa = Math.pow(Math.tan(ea), 2);
			byn = Math.sqrt((Math.pow(directionWeight, 2) * tanSquaredEa) / (1 + tanSquaredEa));
			bxn = byn / Math.tan(ea);
		}
		coeffs.values[1][counter] = new Fraction(1);
		values.values[0][counter] = new Fraction((int)(bx1 * 100000), 100000);
		counter++;
		coeffs.values[numUnknowns / 2 + 1][counter] = new Fraction(1);
		values.values[0][counter] = new Fraction((int)(by1 * 100000), 100000);
		counter++;
		coeffs.values[numUnknowns / 2 - 3][counter] = new Fraction(1);
		coeffs.values[numUnknowns / 2 - 2][counter] = new Fraction(2);
		coeffs.values[numUnknowns / 2 - 1][counter] = new Fraction(3);
		values.values[0][counter] = new Fraction((int)(bxn * 100000), 100000);
		counter++;
		coeffs.values[numUnknowns - 3][counter] = new Fraction(1);
		coeffs.values[numUnknowns - 2][counter] = new Fraction(2);
		coeffs.values[numUnknowns - 1][counter] = new Fraction(3);
		values.values[0][counter] = new Fraction((int)(byn * 100000), 100000);
		counter++;
		Matrix solutions = Matrix.gaussianEliminate(coeffs, values);
		for(int i = 0; i < equations.length; i++){
			int startX = i * 4;
			int startY = i * 4 + numUnknowns / 2;
			equations[i] = new CubicEquation(solutions.values[0][startX].evaluate(), solutions.values[0][startX + 1].evaluate(), solutions.values[0][startX + 2].evaluate(), solutions.values[0][startX + 3].evaluate(), solutions.values[0][startY].evaluate(), solutions.values[0][startY + 1].evaluate(), solutions.values[0][startY + 2].evaluate(), solutions.values[0][startY + 3].evaluate());
		}
	}
	
	private ArrayList<DoublePoint> clone(ArrayList<DoublePoint> points){
		ArrayList<DoublePoint> copy = new ArrayList<DoublePoint>();
		for(DoublePoint p: points){
			copy.add(new DoublePoint(p.x, p.y));
		}
		return copy;
	}
	
	public DoublePoint getSplinePosition(double t){
		t *= points.size() - 1;
		int lowerBound = (int)Math.floor(t);
		if(lowerBound < 0){
			lowerBound = 0;
		}
		else if(lowerBound > equations.length - 1){
			lowerBound = equations.length - 1;
		}
		DoublePoint result = equations[lowerBound].evaluate(t - lowerBound);
		result.x /= POINT_SCALING_FACTOR;
		result.y /= POINT_SCALING_FACTOR;
		return result;
	}
	
}
