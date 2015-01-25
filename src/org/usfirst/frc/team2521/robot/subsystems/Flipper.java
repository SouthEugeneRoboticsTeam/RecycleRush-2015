package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
/**
 *
 */
public class Flipper extends Subsystem {
    DoubleSolenoid flipper;
    BufferedWriter logWriter = null;
    String pathPart1;
	String pathPart2;
	String pathPart3;
    
    private boolean isUp = false;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Flipper() {
    	/*flipUp = new Solenoid(RobotMap.FLIPPER_DOWN_CHANNEL);
    	flipDown = new Solenoid(RobotMap.FLIPPER_UP_CHANNEL);
    	resetFlipper(); */
    	flipper = new DoubleSolenoid(RobotMap.FLIPPER_UP_CHANNEL, RobotMap.FLIPPER_DOWN_CHANNEL);
    	pathPart1 = "/home/lvuser/data/flipper_";
    	pathPart2 = Robot.sensors.pathPart2();
		pathPart3 = ".csv";
    }
    
    public boolean isUp() {
        return isUp;
    }
    
    public void flipperUp() {
        if (isUp != true){
        flipper.set(DoubleSolenoid.Value.kForward);
        isUp = true;
        }
    }
    
    public void flipperDown() {
    	if (isUp = true){
        isUp = false;
        flipper.set(DoubleSolenoid.Value.kReverse);
    	}
        
    }
    
    public void resetFlipper() {
        isUp = false;
    	flipper.set(DoubleSolenoid.Value.kOff);
    
    }

    public void flipperLog(){
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
					isUp + "," +
					flipper.get() + "\n"));
			logWriter.flush();
			}
		} catch (IOException ex) {}
	}
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

