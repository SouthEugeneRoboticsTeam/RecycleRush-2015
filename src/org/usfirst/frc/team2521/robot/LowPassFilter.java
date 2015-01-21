package org.usfirst.frc.team2521.robot;

import edu.wpi.first.wpilibj.Gyro;



public class LowPassFilter {
	//the higher the value, the longer ago it was gotten
	private double[] gyroValue;
	private Gyro gyro;
	double angle;

	public LowPassFilter(Gyro gyro){
		this.gyro = gyro;
		gyroValue = new double[5];
	}
	
	public double getAngle(){
		for(int iii = 1 ; iii <= 4 ; iii++){
			gyroValue[iii] = iii - 1;
		}
		gyroValue[0] = gyro.getAngle();
		angle = (5*gyroValue[0] + 4*gyroValue[1] + 3*gyroValue[2]+ 2*gyroValue[3] + gyroValue[4])/15;
		return angle;
	}

}
