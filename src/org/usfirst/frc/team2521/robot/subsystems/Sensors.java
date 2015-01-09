package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.commands.WriteSensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sensors extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private Gyro gyro;
	private AnalogInput ultrasonic;
	
	public Sensors() {
		gyro = new Gyro(0);
		ultrasonic = new AnalogInput(1);
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
	
	public double getUltrasonicVoltage() {
		return ultrasonic.getVoltage();
	}
	
	public double getUltrasonicDistance() {
		double distance = ultrasonic.getVoltage()*(8.503401361); // Unit conversion 9.8mV/in=8.503ft/V
		return distance;
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new WriteSensors());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

