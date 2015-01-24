package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private Talon conveyor0;
	private Talon conveyor1;
	
	public Conveyor() {
		conveyor0 = new Talon(0);
		conveyor1 = new Talon(1);
		
	}
	
	
	public void moveConveyor(double speed) {
		conveyor0.set(speed);
		conveyor1.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

