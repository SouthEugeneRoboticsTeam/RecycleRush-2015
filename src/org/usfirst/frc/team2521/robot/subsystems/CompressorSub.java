package org.usfirst.frc.team2521.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.ToggleCompressor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class CompressorSub extends Subsystem {
	private Compressor compressor;
	BufferedWriter logWriter = null;
	String pathPart1;
	String pathPart2;
	String pathPart3;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public CompressorSub() {
		compressor = new Compressor();
	}

	
	public void setClosedLoopControl(boolean on){
		compressor.setClosedLoopControl(on);
	}
	
	public void compLog(){
		/*Robot.fileManager.createLog("/home/lvuser/data/compressor_", Timer.getFPGATimestamp() + "," + 
				compressor.getCompressorCurrent() + "," +
				compressor.getPressureSwitchValue() + "\n");*/
	}
	
	
	
	
    public void initDefaultCommand() {
    	
        // Set the default command for a subsystem here.
        setDefaultCommand(new ToggleCompressor());
    }
}

