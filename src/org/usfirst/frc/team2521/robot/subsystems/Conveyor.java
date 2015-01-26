package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.ConveyorLog;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private Talon conveyor0;
	private Talon conveyor1;
	BufferedWriter logWriter = null;
	String path;
	public static boolean upperLimitReached;
	public static boolean lowerLimitReached;
	private DigitalInput limitSwitchTop;
	//private DigitalInput limitSwitchBot;
	
	
	public Conveyor() {
		conveyor0 = new Talon(0);
		conveyor1 = new Talon(1);
		upperLimitReached = false;
		lowerLimitReached = false;
		limitSwitchTop = new DigitalInput(RobotMap.LIMIT_SWITCH_PORT_TOP);
		//limitSwitchBot = new DigitalInput(RobotMap.LIMIT_SWITCH_PORT_BOT);
		
	}
	
	
	public void moveConveyor(double speed) {
		//speed = SmartDashboard.getNumber("Speed");
		//SmartDashboard.putNumber("Conveyor speed", speed);
		if (speed > 0 && !canMoveUp()) {
			conveyor0.set(0);
			conveyor1.set(0);
		} else {
			conveyor0.set(speed);
			conveyor1.set(speed);
		}
	}

	public boolean canMoveUp() {
		return limitSwitchTop.get();
	}
	
	public void conveyorLog(){
		Robot.fileManager.createLog("/home/lvuser/data/conveyor_", Timer.getFPGATimestamp() + "," + 
				conveyor0.get() + "," +
				conveyor1.get() + "," +
				upperLimitReached + "," +
				lowerLimitReached + "\n");
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorLog());
    }
}

