package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;

import org.usfirst.frc.team2521.robot.ComplementaryFilter;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.WriteSensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sensors extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private Gyro gyro;
	private BuiltInAccelerometer accel;
	private AnalogInput ultrasonic;
	private double smoothedDistance = 0;
	private ComplementaryFilter compFilter;
	File record;
	BufferedWriter writer = null;
	
	
	public Sensors() {
		gyro = new Gyro(RobotMap.GYRO_PORT);
		ultrasonic = new AnalogInput(RobotMap.ULTRASONIC_PORT);
		accel = new BuiltInAccelerometer();
		compFilter = new ComplementaryFilter(gyro, accel, 1);
		
	}
	public void createFileWriter() {
		String path = "/home/admin/gyro.out";
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error occured creating file:\n" + e);
			}
		}
    	try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			System.out.println("Error occured creating writer:\n" + e);
		} 
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
	
	public double getUltrasonicVoltage() {
		return ultrasonic.getVoltage();
	}
	
	public double getUltrasonicDistance() {
		double distance = ultrasonic.getVoltage()*(8.503401361); // Unit conversion 9.8mV/in=8.503ft/V
		return distance;
	}
	
	private double lowPass(double newDistance) {
		return smoothedDistance += (newDistance - smoothedDistance) / RobotMap.LOW_PASS_ALPHA;
	}
	
	public double getSmoothedUltrasonicDistance() {
		lowPass(getUltrasonicVoltage());
		return smoothedDistance;
	}
	
	public double getComplementaryAngle() {
		return compFilter.getAngle();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public void writeSensorsToFile() {
		if (writer == null) {
			String path = "/tmp/gyro.csv";
			File file = new File(path);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				
				}
			}
	    	try {
				writer = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				
			} 
		}
		try {
			if (writer != null) {
			writer.write((Timer.getFPGATimestamp() + "," + 
					gyro.getRate() + "," + 
					gyro.getAngle() + "," + 
					accel.getX() +  "," + 
					accel.getY() + "," +
					compFilter.getAngle() + "\n"));
			writer.flush();
			}
		} catch (IOException ex) {}
	} 
	
    public void initDefaultCommand() {
    	setDefaultCommand(new WriteSensors());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

