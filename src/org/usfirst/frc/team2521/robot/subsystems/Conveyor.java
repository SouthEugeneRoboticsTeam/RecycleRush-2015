package org.usfirst.frc.team2521.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.ConveyorTeleop;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon master;
	private CANTalon slave;
	BufferedWriter logWriter = null;
	String logPath;
	String autoPath = "/home/lvuser/auto/Conveyor.csv";
	public static boolean upperLimitReached;
	public static boolean lowerLimitReached;
	BufferedWriter writer = null;
	private DigitalInput limitSwitchTop;
	double[] record;
	//private DigitalInput limitSwitchBot;
	int autoCounter = 0;
	
	
	public Conveyor() {
		master = new CANTalon(RobotMap.CONVEYOR_MASTER);
		slave = new CANTalon(RobotMap.CONVEYOR_SLAVE);
		master.changeControlMode(ControlMode.PercentVbus);
		slave.changeControlMode(ControlMode.Follower);
		slave.set(RobotMap.CONVEYOR_MASTER);
		upperLimitReached = false;
		lowerLimitReached = false;
		limitSwitchTop = new DigitalInput(RobotMap.LIMIT_SWITCH_PORT_TOP);
		//limitSwitchBot = new DigitalInput(RobotMap.LIMIT_SWITCH_PORT_BOT);
		record = new double[RobotMap.TOTAL_EXECUTIONS];
		
	}
	
	
	public void moveConveyor(double speed) {
		//speed = SmartDashboard.getNumber("Speed");
		//SmartDashboard.putNumber("Conveyor speed", speed);
		/*if (Robot.sensors.getCurrent(RobotMap.CONVEYOR_PDP) >= RobotMap.TOTE_CURRENT){
			speed = RobotMap.CONVEYOR_SPEED_HI;
		} else speed = RobotMap.CONVEYOR_SPEED_LO;*/
		if (speed > 0 && !canMoveUp()) {
			master.set(0);
			slave.set(0);
		} else {
			master.set(speed);
			slave.set(speed);
		}
		SmartDashboard.putNumber("Belt value", master.get());
	}

	public boolean canMoveUp() {
		return limitSwitchTop.get();
	}
	
	public void conveyorLog(){
		Robot.fileManager.createLog("/home/lvuser/data/conveyor_", Timer.getFPGATimestamp() + "," + 
				master.get() + "," +
				slave.get() + "," +
				upperLimitReached + "," +
				lowerLimitReached + "\n");
	}
	
	public void writeToFileSetUp() {
		File File = new File(autoPath);
		if (!File.exists()) {
			try {
				File.createNewFile();
			} catch (IOException e) {}
		}
		if (writer == null) {
	    	try {
				writer = new BufferedWriter(new FileWriter(File));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void autoInit(){
		record = Robot.fileManager.csvFileToArray(autoPath);
	}
	
	public void auto(){
		if (autoCounter < RobotMap.TOTAL_EXECUTIONS) {
			master.set(record[autoCounter]);
			SmartDashboard.putNumber("Belt value", record[autoCounter]);
			autoCounter++;
		}
	}
	
	public void writeToFile() {
		try {
			if (writer != null) {
				writer.write(master.get() + ", ");
				writer.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ConveyorTeleop());
    }
}

