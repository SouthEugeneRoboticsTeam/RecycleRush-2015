package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
/**
 *
 */
public class Flipper extends Subsystem {
    Solenoid flipper;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Flipper() {
    	flipper = new Solenoid(RobotMap.FLIPPER_CHANNEL);
    }

    public void turnOn(){
    	flipper.set(true);
    }
    
    public void turnOff(){
    	flipper.set(false);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

