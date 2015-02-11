package org.usfirst.frc.team2521.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class bildrGyro {
	double now;
	double lastUpdate;
	ADXL345_I2C_SparkFun acc;
	GyroITG3200 gyro;
	float sampleFreq;
	
	public bildrGyro() {
		acc = new ADXL345_I2C_SparkFun(I2C.Port.kOnboard, Accelerometer.Range.k16G);
		gyro = new GyroITG3200(I2C.Port.kOnboard);
		now = 0;
		lastUpdate = 0;
	}
	
	public float[] getEuler() {
		float[] q = new float[4]; // quaternion
		float[] angles = new float[3];
		 q = getQ();
		 angles[0] = (float) (Math.atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0]*q[0] + 2 * q[1] * q[1] - 1) * 180/Math.PI); // psi
		 angles[1] = (float) (-Math.asin(2 * q[1] * q[3] + 2 * q[0] * q[2]) * 180/Math.PI); // theta
		 angles[2] = (float) (Math.atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1) * 180/Math.PI); // phi
		 return angles;
		}
	
	public float[] getAngles() {
		float[] angles = new float[3];
		 float[] a = getEuler();

		 angles[0] = a[0];
		 angles[1] = a[1];
		 angles[2] = a[2];
		 
		 if(angles[0] < 0)angles[0] += 360;
		 if(angles[1] < 0)angles[1] += 360;
		 if(angles[2] < 0)angles[2] += 360;
		 return angles;
		}
	
	public float[] getQ() {
		 float[] q = new float[4];
		 float[] val = new float[6];
		 val = getValues();
		 
		 /*
		 DEBUG_PRINT(val[3] * M_PI/180);
		 DEBUG_PRINT(val[4] * M_PI/180);
		 DEBUG_PRINT(val[5] * M_PI/180);
		 DEBUG_PRINT(val[0]);
		 DEBUG_PRINT(val[1]);
		 DEBUG_PRINT(val[2]);
		 DEBUG_PRINT(val[6]);
		 DEBUG_PRINT(val[7]);
		 DEBUG_PRINT(val[8]);
		 */
		 
		 
		 now = Timer.getFPGATimestamp();
		 sampleFreq = (float) (1.0 / ((now - lastUpdate) / 1000000.0));
		 lastUpdate = now;
		 // gyro values are expressed in deg/sec, the * M_PI/180 will convert it to radians/sec
		 //AHRSupdate(val[3] * M_PI/180, val[4] * M_PI/180, val[5] * M_PI/180, val[0], val[1], val[2], val[6], val[7], val[8]);
		 // use the call below when using a 6DOF IMU
		 AHRSupdate(val[3] * Math.PI/180, val[4] * Math.PI/180, val[5] * Math.PI/180, val[0], val[1], val[2], 0, 0, 0);
		 q[0] = q0;
		 q[1] = q1;
		 q[2] = q2;
		 q[3] = q3;
		}
	
	float[] getValues() { 
		 float[] values = new float[6];
		 //int[] accval = new int[3];
		 values[0] = ((float) acc.getX());
		 values[1] = ((float) acc.getY());
		 values[2] = ((float) acc.getZ());
		 values[3] = ((float) gyro.getRotationX());
		 values[4] = ((float) gyro.getRotationY());
		 values[5] = ((float) gyro.getRotationZ());
		 
		 //magn.getValues(&values[6]);
		 return values;
		}

}
