package frc.team4069.robot.spline;

import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.spline.SplinePathGenerator;
import frc.team4069.robot.spline.DoublePoint;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.File;

public class SplineFileReader{
	
	public static SplinePath readSpline(String name){
		byte[] bytes = new byte[(int)(new File(name).length())];
		try{
			FileInputStream fis = new FileInputStream(name + ".dat");
			fis.read(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String splineData = new String(bytes);
		String[] points = splineData.split("\n");
		ArrayList<DoublePoint> doublePoints = new ArrayList<DoublePoint>();
		for(int i = 0; i < points.length; i++){
			String[] current = points[i].split(",");
			double x = Double.parseDouble(current[0]);
			double y = Double.parseDouble(current[1]);
			doublePoints.add(new DoublePoint(x, y));
		}
		return new SplinePath(doublePoints);
	}
	
}