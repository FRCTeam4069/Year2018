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
			splineData += current.x + "," + current.y;
			if(i != spg.pointsOnCurve.length - 1){
				splineData += "\r\n";
			}
		}
		byte[] bytes = splineData.getBytes();
		try{
			FileOutputStream fos = new FileOutputStream("/home/lvuser/" + name + ".dat");
			fos.write(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}