package org.usfirst.frc.team2521.robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team2521.robot.LowPassFilter;

public class DataBasedFilter {
	
	private Gyro gyro;
	private BuiltInAccelerometer accel;
	private double angle = 0;
	private double time = Timer.getFPGATimestamp();
	double error;
	LowPassFilter lpFilter;
	
	
	public DataBasedFilter(Gyro gyro, BuiltInAccelerometer accel) {
		this.gyro = gyro;
		this.accel = accel;
		lpFilter = new LowPassFilter(gyro);
	
	}
	
	public double getAngle() { 
		time = Timer.getFPGATimestamp();
		error = (0.0004*Math.pow(time, 2) + 0.0189*time - 0.4664);
		angle = lpFilter.getAngle() - (0.0004*Math.pow(time, 2) + 0.0189*time - 0.4664);
		return angle;
	}
	
	public double getError(){
		return error;
	}


}
