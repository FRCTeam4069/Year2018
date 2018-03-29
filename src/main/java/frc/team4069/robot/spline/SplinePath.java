package frc.team4069.robot.spline;

import java.util.ArrayList;

public class SplinePath{
	
	private ArrayList<DoublePoint> points;
	
	public int startAngle, endAngle, angleWeight;
	
	private SplinePathGenerator spg;
	
	public static SplinePath splinePathCircle;
	public static SplinePath splinePathCubeLeft;
	public static SplinePath splinePathCubeRight;
	public static SplinePath splinePathLL1;
	public static SplinePath splinePathLL2;
	public static SplinePath splinePathLR2;
	public static SplinePath splinePathRL1;
	public static SplinePath splinePathRL2;
	public static SplinePath splinePathScaleLeft;
	public static SplinePath splinePathScaleRight;
	public static SplinePath splinePathSwitchLeft;
	public static SplinePath splinePathSwitchLeftOld;
	public static SplinePath splinePathSwitchRight;
	public static SplinePath splinePathTeleopExchange;
	public static SplinePath splinePathTeleopExchangeFarSwitch;
	public static SplinePath splinePathTeleopExchangeFarScale;
	public static SplinePath splinePathTeleopScale;
	
	static{
		/*splinePathCircle = new SplinePathCircle();
		splinePathCubeLeft = new SplinePathCubeLeft();
		splinePathCubeRight = new SplinePathCubeRight();
		splinePathLL1 = new SplinePathLL1();
		splinePathLL2 = new SplinePathLL2();
		splinePathLR2 = new SplinePathLR2();*/
		splinePathRL1 = new SplinePathRL1();
		splinePathRL2 = new SplinePathRL2();
		//splinePathScaleLeft = new SplinePathScaleLeft();
		//splinePathScaleRight = new SplinePathScaleRight();
		//splinePathSwitchLeft = new SplinePathSwitchLeft();
		//splinePathSwitchLeftOld = new SplinePathSwitchLeftOld();
		//splinePathSwitchRight = new SplinePathSwitchRight();
		splinePathTeleopExchange = new SplinePathTeleopExchange();
		splinePathTeleopExchangeFarSwitch = new SplinePathTeleopExchangeFarSwitch();
		splinePathTeleopExchangeFarScale = new SplinePathTeleopExchangeFarScale();
		splinePathTeleopScale = new SplinePathTeleopScale();
		/*SplineFileWriter.writeSpline("splinepathcircle", splinePathCircle);
		SplineFileWriter.writeSpline("splinepathcubeleft", splinePathCubeLeft);
		SplineFileWriter.writeSpline("splinepathcuberight", splinePathCubeRight);
		SplineFileWriter.writeSpline("splinepathll1", splinePathLL1);
		SplineFileWriter.writeSpline("splinepathll2", splinePathLL2);
		SplineFileWriter.writeSpline("splinepathlr2", splinePathLR2);
		SplineFileWriter.writeSpline("splinepathrl1", splinePathRL1);
		SplineFileWriter.writeSpline("splinepathrl2", splinePathRL2);
		SplineFileWriter.writeSpline("splinepathscaleleft", splinePathScaleLeft);
		SplineFileWriter.writeSpline("splinepathswitchleft", splinePathSwitchLeft);
		SplineFileWriter.writeSpline("splinepathswitchleftold", splinePathSwitchLeftOld);
		SplineFileWriter.writeSpline("splinepathswitchright", splinePathSwitchRight);
		SplineFileWriter.writeSpline("splinepathteleopexchange", splinePathTeleopExchange);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarswitch", splinePathTeleopExchangeFarSwitch);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarscale", splinePathTeleopExchangeFarScale);
		SplineFileWriter.writeSpline("splinepathteleopscale", splinePathTeleopScale);*/
	}
	
	public SplinePath(ArrayList<DoublePoint> data){
		spg = new SplinePathGenerator(0.55);
		spg.generateSpline(data);
	}
	
	public SplinePath(ArrayList<DoublePoint> points, int startAngle, int endAngle, int angleWeight){
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.angleWeight = angleWeight;
		this.points = points;
		generate();
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
	
	private void generate(){
		spg = new SplinePathGenerator(0.55);
		spg.generateSpline(this);
	}
	
	protected void setPoints(ArrayList<DoublePoint> points){
		this.points = points;
		generate();
	}
	
	public ArrayList<DoublePoint> getPoints(){
		return points;
	}
	
	public SplinePathGenerator getSpline(){
		return spg;
	}
	
}