package frc.team4069.robot.motors;

import edu.wpi.first.wpilibj.Encoder;

public class DriveEncoder extends Encoder{
	
	private int encoderTicksPerRotation = 256;
	
	private int port1, port2;
	
	public DriveEncoder(int port1, int port2){
		super(port1, port2, false, Encoder.EncodingType.k4X);
		this.port1 = port1;
		this.port2 = port2;
		setMaxPeriod(0.1);
		setMinRate(10);
		setDistancePerPulse(5);
		setReverseDirection(true);
		setSamplesToAverage(7);
	}
	
	public double getDistanceTraveledRotations() {
        double quadPosition = get();
        return quadPosition / encoderTicksPerRotation;
    }
	
}