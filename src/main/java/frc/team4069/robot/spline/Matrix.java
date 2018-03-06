package frc.team4069.robot.spline;

import java.awt.Point;

public class Matrix {
	
	public Fraction[][] values;
	
	private Point shape;
	
	public Matrix(int x, int y){
		shape = new Point(x, y);
		values = new Fraction[x][y];
		for(int i = 0; i < x; i++){
			for(int i2 = 0; i2 < y; i2++){
				values[i][i2] = new Fraction();
			}
		}
	}
	
	public Matrix multiply(Matrix other){
		if(shape.x != other.shape.y){
			System.out.println("Matrix dimensions not suitable for multiplication");
			return null;
		}
		Matrix product = new Matrix(other.shape.x, shape.y);
		for(int x = 0; x < product.shape.x; x++){
			for(int y = 0; y < product.shape.y; y++){
				Fraction sum = new Fraction();
				for(int i = 0; i < other.shape.y; i++){
					sum = sum.add(values[i][y].mult(other.values[x][i]));
				}
				product.values[x][y] = sum;
			}
		}
		return product;
	}
	
	public Matrix multiplyScalar(Fraction scalar){
		Matrix scaled = new Matrix(shape.x, shape.y);
		for(int x = 0; x < shape.x; x++){
			for(int y = 0; y < shape.y; y++){
				scaled.values[x][y] = values[x][y].mult(scalar);
			}
		}
		return scaled;
	}
	
	public Matrix addScalar(Fraction scalar){
		Matrix scaled = new Matrix(shape.x, shape.y);
		for(int x = 0; x < shape.x; x++){
			for(int y = 0; y < shape.y; y++){
				scaled.values[x][y] = values[x][y].add(scalar);
			}
		}
		return scaled;
	}
	
	public Matrix add(Matrix other){
		if(shape.x != other.shape.x || shape.y != other.shape.y){
			System.out.println("Invalid matrix dimensions for addition");
			return null;
		}
		Matrix sum = new Matrix(shape.x, shape.y);
		for(int x = 0; x < shape.x; x++){
			for(int y = 0; y < shape.y; y++){
				sum.values[x][y] = values[x][y].add(other.values[x][y]);
			}
		}
		return sum;
	}
	
	public void setRow(int y, Matrix row){
		if(row.shape.x != shape.x || row.shape.y != 1){
			System.out.println("Row dimensions are invalid");
			return;
		}
		for(int x = 0; x < shape.x; x++){
			values[x][y] = row.values[x][0];
		}
	}
	
	public Matrix getRow(int y){
		Matrix row = new Matrix(shape.x, 1);
		for(int x = 0; x < shape.x; x++){
			row.values[x][0] = values[x][y];
		}
		return row;
	}
	
	public Matrix concat(Matrix other){
		if(other.shape.y != shape.y){
			System.out.println("Invalid dimensions for concatenation");
		}
		Matrix result = new Matrix(shape.x + other.shape.x, shape.y);
		for(int x = 0; x < result.shape.x; x++){
			for(int y = 0; y < result.shape.y; y++){
				if(x < shape.x){
					result.values[x][y] = values[x][y];
				}
				else{
					result.values[x][y] = other.values[x - shape.x][y];
				}
			}
		}
		return result;
	}
	
	public void set(int[][] values){
		Fraction[][] fractions = new Fraction[values.length][values[0].length];
		for(int x = 0; x < values.length; x++){
			for(int y = 0; y < values[0].length; y++){
				fractions[x][y] = new Fraction(values[x][y], 1);
			}
		}
		if(values.length == this.values.length && values[0].length == this.values[0].length){
			this.values = fractions;
		}
		else{
			System.out.println("Invalid dimensions for matrix value assignment");
		}
	}
	
	/**
	 * Run gaussian elimination in order to generate solutions to the system of equations
	 * @param coeffs
	 * @param values
	 * @return
	 */
	public static Matrix gaussianEliminate(Matrix coeffs, Matrix values){
		if(coeffs.shape.y != values.shape.y || values.shape.x != 1){
			System.out.println("Invalid dimensions for gaussian elimination");
			return null;
		}
		Matrix mat = coeffs.concat(values);
		for(int x = 0; x < coeffs.shape.x; x++){
			double maxElement = Math.abs(mat.values[x][x].evaluate());
			int maxRow = x;
			for(int y = x + 1; y < coeffs.shape.y; y++){
				if(Math.abs(mat.values[x][y].evaluate()) > maxElement){
					maxElement = Math.abs(mat.values[x][y].evaluate());
					maxRow = y;
				}
			}
			Matrix temp = mat.getRow(x);
			mat.setRow(x, mat.getRow(maxRow));
			mat.setRow(maxRow, temp);
			for(int y = x + 1; y < coeffs.shape.y; y++){
				Fraction c = mat.values[x][y].div(mat.values[x][x]).mult(-1);
				mat.setRow(y, mat.getRow(y).add(mat.getRow(x).multiplyScalar(c)));
			}
		}
		for(int i = 0; i < coeffs.shape.x; i++){
			if(mat.values[i][i].evaluate() != 1){
				mat.setRow(i, mat.getRow(i).multiplyScalar(mat.values[i][i].reciprocal()));
			}
		}
		Matrix solutions = new Matrix(1, coeffs.shape.y);
		for(int i = 0; i < solutions.shape.y; i++){
			if(i == 0){
				solutions.values[0][solutions.shape.y - i - 1] = mat.values[mat.shape.x - 1][solutions.shape.y - i - 1];
			}
			else{
				Fraction sum = new Fraction();
				for(int x = solutions.shape.y - i; x < coeffs.shape.x; x++){
					sum = sum.sub(mat.values[x][solutions.shape.y - i - 1].mult(solutions.values[0][x]));
				}
				solutions.values[0][solutions.shape.y - i - 1] = sum.add(mat.values[mat.shape.x - 1][solutions.shape.y - i - 1]);
			}
		}
		return solutions;
	}
	
	public static Matrix identity(int n){
		Matrix id = new Matrix(n, n);
		for(int i = 0; i < n; i++){
			id.values[i][i] = new Fraction(1, 1);
		}
		return id;
	}
	
	@Override
	public String toString(){
		String mat = "";
		for(int y = 0; y < shape.y; y++){
			String row = "[";
			for(int x = 0; x < shape.x; x++){
				row += values[x][y] + (x == shape.x - 1 ? "" : " ");
			}
			row += "]";
			mat += row + (y == shape.y - 1 ? "" : "\n");
		}
		return mat;
	}
	
}
