package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lights extends Subsystem {
	private DigitalOutput allLights;
	private DigitalOutput light1;
	private DigitalOutput light2;
	private DigitalOutput light3;
	private DigitalOutput light4;
	private DigitalOutput light5;
	private DigitalOutput light6;
	private DigitalOutput light7;
	
	public void Lights(){
		allLights = new DigitalOutput(RobotMap.LIGHT_MAIN);
		light1 = new DigitalOutput(RobotMap.LIGHT_1);
		light2 = new DigitalOutput(RobotMap.LIGHT_2);
		light3 = new DigitalOutput(RobotMap.LIGHT_3);
		light4 = new DigitalOutput(RobotMap.LIGHT_4);
		light5 = new DigitalOutput(RobotMap.LIGHT_5);
		light6 = new DigitalOutput(RobotMap.LIGHT_6);
		light7 = new DigitalOutput(RobotMap.LIGHT_7);
	}
    
	public void turnAllOn(){
		allLights.set(false);
		light1.set(true);
		light2.set(true);
		light3.set(true);
		light4.set(true);
		light5.set(true);
		light6.set(true);
		light7.set(true);
	}
	
	public void turnAllOff(){
		allLights.set(false);
		light1.set(false);
		light2.set(false);
		light3.set(false);
		light4.set(false);
		light5.set(false);
		light6.set(false);
		light7.set(false);
	}
	
	public void turnOn(int lightNumber){
		switch (lightNumber) {
		case 1:
			light1.set(true);
			break;
		case 2:
			light2.set(true);
			break;
		case 3:
			light3.set(true);
			break;
		case 4:
			light4.set(true);
			break;
		case 5:
			light5.set(true);
			break;
		case 6:
			light6.set(true);
			break;
		case 7:
			light7.set(true);
			break;
		default:
			break;
		}
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

