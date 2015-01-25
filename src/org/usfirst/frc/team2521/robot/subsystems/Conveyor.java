package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.ConveyorLog;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private Talon conveyor0;
	private Talon conveyor1;
	BufferedWriter logWriter = null;
	String pathPart1;
	String pathPart2;
	String pathPart3;
	
	
	public Conveyor() {
		conveyor0 = new Talon(0);
		conveyor1 = new Talon(1);
		pathPart1 = "/home/lvuser/data/conveyor_";
		pathPart2 = Robot.sensors.pathPart2();
		pathPart3 = ".csv";
		
	}
	
	
	public void moveConveyor(double speed) {
		conveyor0.set(speed);
		conveyor1.set(speed);
	}

	
	public void conveyorLog(){
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
					conveyor0.get() + "," +
					conveyor1.get() + "\n"));
			logWriter.flush();
			}
		} catch (IOException ex) {}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorLog());
    }
}

