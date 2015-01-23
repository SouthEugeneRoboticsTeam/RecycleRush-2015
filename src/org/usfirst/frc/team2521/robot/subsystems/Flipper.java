package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
/**
 *
 */
public class Flipper extends Subsystem {
    DoubleSolenoid flipper;
    
    private boolean isUp = false;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Flipper() {
    	/*flipUp = new Solenoid(RobotMap.FLIPPER_DOWN_CHANNEL);
    	flipDown = new Solenoid(RobotMap.FLIPPER_UP_CHANNEL);
    	resetFlipper(); */
    	flipper = new DoubleSolenoid(RobotMap.FLIPPER_UP_CHANNEL, RobotMap.FLIPPER_DOWN_CHANNEL);
    }
    
    public boolean isUp() {
        return isUp;
    }
    
    public void flipperUp() {
        if (isUp != true){
        flipper.set(DoubleSolenoid.Value.kForward);
        isUp = true;
        }
    }
    
    public void flipperDown() {
    	if (isUp = true){
        isUp = false;
        flipper.set(DoubleSolenoid.Value.kReverse);
    	}
        
    }
    
    public void resetFlipper() {
        isUp = false;
    	flipper.set(DoubleSolenoid.Value.kOff);
    
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

