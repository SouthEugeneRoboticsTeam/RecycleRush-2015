package org.usfirst.frc.team2521.robot;

import javax.rmi.CORBA.Util;
import javax.swing.plaf.SliderUI;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class ComplementaryFilter {
	
	private Gyro gyro;
	private BuiltInAccelerometer accel;
	private double angle = 0;
	private double lastTime;
	private double tau;
	private double gyroAngle;
	
	public ComplementaryFilter(Gyro gyro, BuiltInAccelerometer accel, double tau) {
		this.gyro = gyro;
		this.accel = accel;
		this.tau = 100000; 
	}
	
	public double getAngle() {
		lastTime = Timer.getFPGATimestamp();
		double dt =  Timer.getFPGATimestamp() - lastTime;
		double alpha = (tau/dt)/(1+tau/dt);
		double accAngle = Math.atan2(accel.getY(), accel.getX())*180/Math.PI;
		double omega = gyro.getRate();
//		angle = alpha*angle + alpha*omega*dt;
//		double error = angle-accAngle;
//		angle = angle + (1-alpha)*error;
		angle = alpha*angle + (1-alpha)*accAngle +alpha*omega*dt;
		return angle;
	}

}
