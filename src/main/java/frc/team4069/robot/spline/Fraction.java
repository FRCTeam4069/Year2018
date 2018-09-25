package frc.team4069.robot.spline;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Fraction {
	
	public BigInteger numerator, denominator;
	
	public Fraction(BigInteger numerator, BigInteger denominator){
		this.numerator = numerator;
		this.denominator = denominator;
		if(this.denominator.signum() == 0){
			System.out.println("Error: divide by zero in fraction: " + numerator + " / " + denominator);
			System.exit(0);
		}
		if(this.denominator.signum() < 0 && this.numerator.signum() > 0 || this.denominator.signum() < 0 && this.numerator.signum() < 0){
			this.numerator = this.numerator.multiply(BigInteger.valueOf(-1));
			this.denominator = this.denominator.multiply(BigInteger.valueOf(-1));
		}
		simplify();
	}
	
	public Fraction(int numerator, int denominator){
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}
	
	private void simplify(){
		BigInteger num = numerator.abs();
		BigInteger den = denominator.abs();
		if(num.signum() > 0 && den.signum() > 0){
			BigInteger remainder = BigInteger.valueOf(-1);
			while(remainder.signum() != 0){
				BigInteger max = num.compareTo(den) < 0 ? den : num;
				BigInteger min = num.compareTo(den) > 0 ? den : num;
				remainder = max.mod(min);
				num = min;
				den = remainder;
			}
			BigInteger gcd = num.compareTo(den) < 0 ? den : num;
			this.numerator = this.numerator.divide(gcd);
			this.denominator = this.denominator.divide(gcd);
		}
		else if(num.signum() == 0 && den.signum() != 0){
			denominator = denominator.abs().divide(denominator);
		}
	}
	
	public Fraction(BigInteger numerator){
		this(numerator, BigInteger.ONE);
	}
	
	public Fraction(int numerator){
		this(BigInteger.valueOf(numerator));
	}
	
	public Fraction(){
		this(BigInteger.ZERO, BigInteger.ONE);
	}
	
	public Fraction add(Fraction other){
		return new Fraction(this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
	}
	
	public Fraction sub(Fraction other){
		return new Fraction(this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
	}
	
	public Fraction mult(Fraction other){
		return new Fraction(this.numerator.multiply(other.numerator), this.denominator.multiply(other.denominator));
	}
	
	public Fraction mult(int scalar){
		return new Fraction(this.numerator.multiply(BigInteger.valueOf(scalar)), this.denominator);
	}
	
	public Fraction div(Fraction other){
		return new Fraction(this.numerator.multiply(other.denominator), this.denominator.multiply(other.numerator));
	}
	
	public Fraction reciprocal(){
		if(this.numerator.signum() == 0){
			System.out.println("Error in reciprocal: " + this.numerator + " / " + this.denominator);
		}
		return new Fraction(this.denominator, this.numerator);
	}
	
	public double evaluate(){
		return new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), 2, RoundingMode.HALF_UP).doubleValue();
	}
	
	public String toString(){
		return "" + evaluate();
	}
	
}
