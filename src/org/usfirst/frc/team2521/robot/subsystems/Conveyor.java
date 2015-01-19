package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Conveyor extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANJaguar conveyor;
	
	public Conveyor() {
		conveyor = new CANJaguar(RobotMap.CONVEYOR_MOTOR);
		conveyor.setPercentMode();
		conveyor.enableControl(); 
	}
	
	
	public void moveConveyor(double speed) {
		conveyor.set(speed);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

