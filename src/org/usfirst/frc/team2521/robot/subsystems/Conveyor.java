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
	private Talon conveyor;
	private Talon conveyor2;
	
	public Conveyor() {
		conveyor = new Talon(0);
		conveyor2 = new Talon(1);
		
	}
	
	
	public void moveConveyor(double speed) {
		conveyor.set(speed);
		conveyor2.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

