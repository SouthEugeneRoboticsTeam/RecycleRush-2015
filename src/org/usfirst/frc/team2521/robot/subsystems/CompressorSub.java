package org.usfirst.frc.team2521.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;

import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.StartCompressor;

/**
 *
 */
public class CompressorSub extends Subsystem {
	Compressor compressor;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public CompressorSub() {
		compressor = new Compressor(RobotMap.PCM_CAN_CHANNEL);
	}
	
	public void startCompressor(){
		compressor.start();
	}
	
	public void stopCompressor(){
		compressor.stop();
	}

    public void initDefaultCommand() {
    	
        // Set the default command for a subsystem here.
        setDefaultCommand(new StartCompressor());
    }
}

