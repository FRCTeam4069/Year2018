package frc.team4069.robot.spline;

import frc.team4069.robot.spline.SplinePath;
import frc.team4069.robot.spline.SplinePathGenerator;
import java.io.FileOutputStream;

public class SplineFileWriter{
	
	public static void writeSpline(String name, SplinePath path){
		String splineData = "";
		SplinePathGenerator spg = path.getSpline();
		for(int i = 0; i < spg.pointsOnCurve.length; i++){
			DoublePoint current = spg.pointsOnCurve[i];
		}
		byte[] bytes = splineData.getBytes();
		try{
			FileOutputStream fos = new FileOutputStream(name + ".dat");
			fos.write(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}