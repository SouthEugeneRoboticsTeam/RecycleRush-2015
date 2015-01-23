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
import org.usfirst.frc.team2521.robot.DataBasedFilter;
import org.usfirst.frc.team2521.robot.LowPassFilter;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.WriteSensors;
import org.usfirst.frc.team2521.robot.commands.*;

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
	private AnalogInput blankspace;
	private double smoothedDistance = 0;
	private ComplementaryFilter compFilter;
	private DataBasedFilter dbFilter;
	private LowPassFilter lpFilter;
	File record;
	BufferedWriter gyroWriter = null;
	BufferedWriter genWriter = null;
	
	
	public Sensors() {
		gyro = new Gyro(RobotMap.GYRO_PORT);
		blankspace = new AnalogInput(RobotMap.EMPTY_ANALOG);
		ultrasonic = new AnalogInput(RobotMap.ULTRASONIC_PORT);
		accel = new BuiltInAccelerometer();
		compFilter = new ComplementaryFilter(gyro, accel, 1);
		dbFilter = new DataBasedFilter(gyro, accel);
		lpFilter = new LowPassFilter(gyro);
		
	}
	/*public void createFileWriter() {
		String path = "/home/admin/gyro.out";
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error occurred creating file:\n" + e);
			}
		}
    	try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			System.out.println("Error occurred creating writer:\n" + e);
		} 
	}*/
	
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
	
	public double getDataBasedAngle() {
		return dbFilter.getAngle();
	}
	
	public double getDBFilterError() {
		return dbFilter.getError();
	}
	
	public double getLPAngle(){
		return lpFilter.getAngle();
	}
	
	public void resetGyro() {
		gyro.reset();
	}
	
	public double getBlank(){
		return blankspace.getValue();
	}
	
	public void writeSensorsToFile() {
		if (gyroWriter == null) {
			String path = "/tmp/gyro.csv";
			File file = new File(path);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				
				}
			}
	    	try {
				gyroWriter = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				
			} 
		}
		try {
			if (gyroWriter != null) {
			gyroWriter.write((Timer.getFPGATimestamp() + "," + 
					gyro.getRate() + "," + 
					gyro.getAngle() + "," + 
					accel.getX() +  "," + 
					accel.getY() + "," +
					compFilter.getAngle() + "," +
					dbFilter.getAngle() + "\n"));
			gyroWriter.flush();
			}
		} catch (IOException ex) {}
	} 
	
	/*public void generalLog(){
		if (genWriter == null) {
			String path = "/tmp/log.csv";
			File file = new File(path);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				
				}
			}
	    	try {
				genWriter = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				
			} 
		}
		try {
			if (genWriter != null) {
			genWriter.write((Timer.getFPGATimestamp() + "," + 
					dbFilter.getAngle() + "\n"));
			gyroWriter.flush();
			}
		} catch (IOException ex) {}
	} */
	
	
    public void initDefaultCommand() {
    	setDefaultCommand(new WriteSensors());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	// This is a very simple weighted average filter
	public double GetSimpleFilter( double RA[] )
	{
		double result = 0;
		
		int len = RA.length;
		
		double val_n = RA[ len - 1 ];
		double val_n1 = RA[ len - 2 ];
		double val_n2 = RA[ len - 3 ];
		double val_n3 = RA[ len - 4 ];
		
		double weightN = 5;
		double weightN1 = 4;
		double weightN2 = 3;
		double weightN3 = 1
				;
		double sum = val_n * weightN;
		sum += val_n1 * weightN1;
		sum += val_n2 * weightN2;
		sum += val_n3 * weightN3;
		
		result = sum / ( weightN + weightN1 + weightN2 + weightN3 );
		
		return result;
	}
}

