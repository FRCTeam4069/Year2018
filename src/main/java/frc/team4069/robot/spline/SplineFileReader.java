package frc.team4069.robot.spline;

import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.spline.SplinePathGenerator;
import frc.team4069.robot.spline.DoublePoint;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.File;

public class SplineFileReader{
	
	public SplineFileReader(){
		
	}
	
	public SplinePath readSpline(String name){
		byte[] bytes = new byte[(int)(new File("/home/lvuser/" + name + ".spo").length())];
		try{
			FileInputStream fis = new FileInputStream("/home/lvuser/" + name + ".spo");
			fis.read(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String splineData = new String(bytes);
		String[] points = splineData.split("\r\n");
		ArrayList<DoublePoint> doublePoints = new ArrayList<DoublePoint>();
		for(int i = 0; i < points.length; i++){
			String[] current = points[i].split(",");
			if(current[0].length() == 0 || current[1].length() == 0){
				continue;
			}
			double x = Double.parseDouble(current[0]);
			double y = Double.parseDouble(current[1]);
			doublePoints.add(new DoublePoint(x, y));
		}
		return new SplinePath(doublePoints);
	}
	
	public SplinePath readSplineInput(String name){
		byte[] bytes = new byte[(int)(new File("/home/lvuser/" + name + ".spi").length())];
		try{
			FileInputStream fis = new FileInputStream("/home/lvuser/" + name + ".spi");
			fis.read(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String splineData = new String(bytes);
		String[] points = splineData.split("\r\n");
		ArrayList<DoublePoint> doublePoints = new ArrayList<DoublePoint>();
		int startAngle = 270;
		int endAngle = 270;
		int weight = 250;
		for(int i = 0; i < points.length; i++){
			if(points[i].contains(",")){
				String[] current = points[i].split(",");
				if(current[0].length() == 0 || current[1].length() == 0){
					continue;
				}
				double x = Double.parseDouble(current[0]);
				double y = Double.parseDouble(current[1]);
				doublePoints.add(new DoublePoint(x, y));
			}
			else{
				if(i == 0){
					startAngle = Integer.parseInt(points[i]);
				}
				else if(i == 1){
					endAngle = Integer.parseInt(points[i]);
				}
				else if(i == 2){
					weight = Integer.parseInt(points[i]);
				}
			}
		}
		return new SplinePath(doublePoints, startAngle, endAngle, weight, true);
	}
	
}