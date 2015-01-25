package org.usfirst.frc.team2521.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.StartCompressor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class CompressorSub extends Subsystem {
	Compressor compressor;
	BufferedWriter logWriter = null;
	String pathPart1;
	String pathPart2;
	String pathPart3;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public CompressorSub() {
		compressor = new Compressor();
		pathPart1 = "/home/lvuser/data/compressor_";
		pathPart2 = Robot.sensors.pathPart2();
		pathPart3 = ".csv";
	}
	
	public void startCompressor(){
		compressor.start();
	}
	
	public void stopCompressor(){
		compressor.stop();
	}

	
	public void compLog(){
		if (logWriter == null) {
			String path = (pathPart1 + pathPart2 + pathPart3);
			File file = new File(path);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
				
				}
			}
	    	try {
				logWriter = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				
			} 
		}
		try {
			if (logWriter != null) {
			logWriter.write((Timer.getFPGATimestamp() + "," + 
					compressor.getCompressorCurrent() + "," +
					compressor.getPressureSwitchValue() + "\n"));
			logWriter.flush();
			}
		} catch (IOException ex) {}
	}
	
    public void initDefaultCommand() {
    	
        // Set the default command for a subsystem here.
        setDefaultCommand(new StartCompressor());
    }
}

