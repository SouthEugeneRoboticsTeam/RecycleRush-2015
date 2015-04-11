package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 */
public class Arm extends Subsystem {
	
	DoubleSolenoid arm;
	boolean isUp = true;
	
	public Arm() {
		arm = new DoubleSolenoid(RobotMap.ARM_DOWN_CHANNEL, RobotMap.ARM_UP_CHANNEL);
	}
	
	public void toggleUp(){
		isUp = !isUp;
		if (isUp){
			arm.set(DoubleSolenoid.Value.kForward);
		} else arm.set(DoubleSolenoid.Value.kReverse);
	}
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
    	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

