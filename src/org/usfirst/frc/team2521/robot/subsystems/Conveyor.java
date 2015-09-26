package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.MaintainConveyor;


import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class Conveyor extends Subsystem {
	private CANTalon master;
	private CANTalon slave;
	private DigitalInput magReadSwitch;
	
	
	public Conveyor() {
		master = new CANTalon(RobotMap.CONVEYOR_MASTER);
		slave = new CANTalon(RobotMap.CONVEYOR_SLAVE);
		
		master.changeControlMode(ControlMode.PercentVbus);
		slave.changeControlMode(ControlMode.Follower);
		slave.set(RobotMap.CONVEYOR_MASTER);
		master.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		master.setPID(3, 0, 1);
		
		magReadSwitch = new DigitalInput(RobotMap.MAG_READ_CHANNEL);
	}
	
	
	public void moveConveyor(double speed) {
		master.changeControlMode(ControlMode.PercentVbus);
		master.enableControl();
		master.set(speed);
	}
	
	public boolean getReadSwitch() {
		return !magReadSwitch.get(); // Switch is pulled to ground when closed
	}
	
	public void resetPosition() {
		currentHook = 0;
		master.setPosition(0);
	}
	
	public void setPID(double p, double i, double d) {
		master.setPID(p, i, d);
	}
	
	public int getPosition() {
		return (int) master.getPosition();
	}
	
	public void setPosition(int position) {
		master.changeControlMode(ControlMode.Position);
		master.set(position);
		master.enableControl();
	}
	
	private int currentHook = 0;
	
	public void moveByTote(int direction) {
		setPID(3, 0, 1);
		int nextHook = currentHook + direction < 0 ? RobotMap.HOOK_POSITIONS.length + direction: currentHook + direction; 
		setPosition(RobotMap.HOOK_POSITIONS[nextHook]);
		currentHook = nextHook;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new MaintainConveyor());
    }
}

