package frc.team4069.robot.spline;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

public class SplinePath{
	
	private ArrayList<DoublePoint> points;
	
	private static boolean shouldGenerate = false;
	
	public int startAngle, endAngle, angleWeight;
	
	private SplinePathGenerator spg;
	
	public static SplinePath splinePathCircle;
	public static SplinePath splinePathCubeLeft;
	public static SplinePath splinePathCubeRight;
	public static SplinePath splinePathLL1;
	public static SplinePath splinePathLR1;
	public static SplinePath splinePathLR2;
	public static SplinePath splinePathRL1;
	public static SplinePath splinePathRL1Shifted;
	public static SplinePath splinePathRL2;
	public static SplinePath splinePathScaleLeft;
	public static SplinePath splinePathScaleRight;
	public static SplinePath splinePathSwitchLeft;
	public static SplinePath splinePathSwitchLeftOld;
	public static SplinePath splinePathSwitchRight;
	public static SplinePath splinePathSwitchRightShifted;
	public static SplinePath splinePathTeleopExchange;
	public static SplinePath splinePathTeleopExchangeFarSwitch;
	public static SplinePath splinePathTeleopExchangeFarScale;
	public static SplinePath splinePathTeleopScale;
	public static SplinePath splinePathTeleopExchangeMirror;
	public static SplinePath splinePathTeleopExchangeFarSwitchMirror;
	public static SplinePath splinePathTeleopExchangeFarScaleMirror;
	public static SplinePath splinePathTeleopScaleMirror;
	
	private static void generateSplines(){
		splinePathCircle = new SplinePathCircle();
		splinePathCubeLeft = new SplinePathCubeLeft();
		splinePathCubeRight = new SplinePathCubeRight();
		splinePathLL1 = new SplinePathLL1();
		splinePathLR1 = new SplinePathLR1();
		splinePathLR2 = new SplinePathLR2();
		splinePathRL1 = new SplinePathRL1();
		splinePathRL1Shifted = new SplinePathRL1Shifted();
		splinePathRL2 = new SplinePathRL2();
		splinePathScaleLeft = new SplinePathScaleLeft();
		splinePathScaleRight = new SplinePathScaleRight();
		splinePathSwitchLeft = new SplinePathSwitchLeft();
		splinePathSwitchLeftOld = new SplinePathSwitchLeftOld();
		splinePathSwitchRight = new SplinePathSwitchRight();
		splinePathSwitchRightShifted = new SplinePathSwitchRightShifted();
		splinePathTeleopExchange = new SplinePathTeleopExchange();
		splinePathTeleopExchangeMirror = new SplinePathTeleopExchange();
		splinePathTeleopExchangeMirror.mirror(false);
		splinePathTeleopExchangeFarSwitch = new SplinePathTeleopExchangeFarSwitch();
		splinePathTeleopExchangeFarSwitchMirror = new SplinePathTeleopExchangeFarSwitch();
		splinePathTeleopExchangeFarSwitchMirror.mirror(false);
		splinePathTeleopExchangeFarScale = new SplinePathTeleopExchangeFarScale();
		splinePathTeleopExchangeFarScaleMirror = new SplinePathTeleopExchangeFarScale();
		splinePathTeleopExchangeFarScaleMirror.mirror(false);
		splinePathTeleopScale = new SplinePathTeleopScale();
		splinePathTeleopScaleMirror = new SplinePathTeleopScale();
		splinePathTeleopScaleMirror.mirror(false);
	}
	
	private static void writeSplines(){
		SplineFileWriter.writeSpline("splinepathcircle", splinePathCircle);
		SplineFileWriter.writeSpline("splinepathcubeleft", splinePathCubeLeft);
		SplineFileWriter.writeSpline("splinepathcuberight", splinePathCubeRight);
		SplineFileWriter.writeSpline("splinepathll1", splinePathLL1);
		SplineFileWriter.writeSpline("splinepathlr1", splinePathLR1);
		SplineFileWriter.writeSpline("splinepathlr2", splinePathLR2);
		SplineFileWriter.writeSpline("splinepathrl1", splinePathRL1);
		SplineFileWriter.writeSpline("splinepathrl1shifted", splinePathRL1Shifted);
		SplineFileWriter.writeSpline("splinepathrl2", splinePathRL2);
		SplineFileWriter.writeSpline("splinepathscaleleft", splinePathScaleLeft);
		SplineFileWriter.writeSpline("splinepathswitchleft", splinePathSwitchLeft);
		SplineFileWriter.writeSpline("splinepathswitchleftold", splinePathSwitchLeftOld);
		SplineFileWriter.writeSpline("splinepathswitchright", splinePathSwitchRight);
		SplineFileWriter.writeSpline("splinepathswitchrightshifted", splinePathSwitchRightShifted);
		SplineFileWriter.writeSpline("splinepathteleopexchange", splinePathTeleopExchange);
		SplineFileWriter.writeSpline("splinepathteleopexchangemirror", splinePathTeleopExchangeMirror);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarswitch", splinePathTeleopExchangeFarSwitch);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarswitchmirror", splinePathTeleopExchangeFarSwitchMirror);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarscale", splinePathTeleopExchangeFarScale);
		SplineFileWriter.writeSpline("splinepathteleopexchangefarscalemirror", splinePathTeleopExchangeFarScaleMirror);
		SplineFileWriter.writeSpline("splinepathteleopscale", splinePathTeleopScale);
		SplineFileWriter.writeSpline("splinepathteleopscalemirror", splinePathTeleopScaleMirror);
	}
	
	private static HashMap<String, SplinePath> splines = new HashMap<String, SplinePath>();

	private static String[] noDeleteInput = {/*"doublescaleright", "doublescaleleft", "doublescalerightmirror", "doublescaleleftmirror"*/};

	private static void readSplinesAutomatically(){
		SplineFileReader spf = new SplineFileReader();
		File folder = new File("/home/lvuser/");
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].isFile()){
				String name = files[i].getName().substring(0, files[i].getName().length() - 4);
				String extension = files[i].getName().substring(files[i].getName().length() - 3);
				if(extension.equals("spo")){
					SplinePath sp = spf.readSpline(name);
					splines.put(name, sp);
				}
				else if(extension.equals("spi")){
					SplinePath sp = spf.readSplineInput(name);
					SplineFileWriter.writeSpline(name, sp);
					if(shouldDeleteInputFile(name)) {
						files[i].delete();
					}
					splines.put(name, sp);
				}
			}
		}
	}

	private static boolean shouldDeleteInputFile(String name){
		for(int i = 0; i < noDeleteInput.length; i++){
			if(noDeleteInput[i].equals(name)){
				return false;
			}
		}
		return true;
	}
	
	private static void clearInputSplines(){
		File folder = new File("/home/lvuser/");
		File[] files = folder.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].isFile()){
				String extension = files[i].getName().substring(files[i].getName().length() - 3);
				if(extension.equals("spi")){
					files[i].delete();
				}
			}
		}
	}
	
	static{
		if(shouldGenerate){
			generateSplines();
			writeSplines();
			clearInputSplines();
			readSplinesAutomatically();
		}
		else{
			readSplinesAutomatically();
		}
	}
	
	public SplinePath(ArrayList<DoublePoint> data){
		spg = new SplinePathGenerator(0.55);
		spg.generateSpline(data);
	}
	
	public SplinePath(ArrayList<DoublePoint> data, boolean mirrorX, boolean mirrorY){
		spg = new SplinePathGenerator(0.55);
		if(mirrorX){
			mirror(true);
		}
		if(mirrorY){
			mirror(false);
		}
		spg.generateSpline(data);
	}
	
	public SplinePath(ArrayList<DoublePoint> points, int startAngle, int endAngle, int angleWeight, boolean generate){
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.angleWeight = angleWeight;
		this.points = points;
		if(generate){
			generate();
		}
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
	
	private void mirror(boolean xDirection){
		for(int i = 0; i < points.size(); i++){
			if(xDirection){
				points.get(i).x *= -1;
			}
			else{
				points.get(i).y *= -1;
			}
		}
		generate();
	}
	
	private void generate(){
		spg = new SplinePathGenerator(0.55);
		spg.generateSpline(this);
	}
	
	protected void setPoints(ArrayList<DoublePoint> points, boolean generate){
		this.points = points;
		if(generate){
			generate();
		}
	}
	
	public ArrayList<DoublePoint> getPoints(){
		return points;
	}
	
	public SplinePathGenerator getSpline(){
		return spg;
	}
	
	public static SplinePath getSplinePath(String name){
		return splines.get(name);
	}
	
}