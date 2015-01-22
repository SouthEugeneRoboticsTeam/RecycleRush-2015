package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
/**
 *
 */
public class Flipper extends Subsystem {
    Solenoid flipUp;
    Solenoid flipDown;
    private boolean isUp = false;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Flipper() {
    	flipUp = new Solenoid(RobotMap.FLIPPER_DOWN_CHANNEL);
    	flipDown = new Solenoid(RobotMap.FLIPPER_UP_CHANNEL);
    	resetFlipper();
    }
    
    public boolean isUp() {
        return isUp;
    }
    
    public void flipperUp() {
        isUp = true;
        flipDown.set(isUp);
    }
    
    public void flipperDown() {
        isUp = false;
        flipDown.set(isUp);
    }
    
    public void resetFlipper() {
        isUp = false;
    	flipUp.set(isUp);
    
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

