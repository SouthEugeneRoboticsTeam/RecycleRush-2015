package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2521.robot.OI;
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
    //DoubleSolenoid flipper;
	BufferedWriter writer = null;
    String path = "/home/lvuser/auto/flipper.csv";
    
    private boolean isUp = false;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Flipper() {
    	/*flipUp = new Solenoid(RobotMap.FLIPPER_DOWN_CHANNEL);
    	flipDown = new Solenoid(RobotMap.FLIPPER_UP_CHANNEL);
    	resetFlipper(); */
    	//flipper = new DoubleSolenoid(RobotMap.FLIPPER_UP_CHANNEL, RobotMap.FLIPPER_DOWN_CHANNEL);
    }
    
    public boolean isUp() {
        return isUp;
    }
    
    public void flipperUp() {
        if (isUp != true){
        //flipper.set(DoubleSolenoid.Value.kForward);
        isUp = true;
        }
    }
    
    public void flipperDown() {
    	if (isUp = true){
        isUp = false;
        //flipper.set(DoubleSolenoid.Value.kReverse);
    	}
        
    }
    
    public void resetFlipper() {
        isUp = false;
    	//flipper.set(DoubleSolenoid.Value.kOff);
    
    }

    
    public void flipperLog(){
		/*Robot.fileManager.createLog("/home/lvuser/data/flipper_", Timer.getFPGATimestamp() + "," + 
				isUp + "," +
				flipper.get() + "\n");*/
	}
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
 
    
    public void writeToFile(){
    	
    }
}

