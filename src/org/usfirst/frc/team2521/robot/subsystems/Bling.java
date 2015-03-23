package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Bling extends Subsystem {
	DigitalOutput bling;
	
	public Bling() {
		bling = new DigitalOutput(RobotMap.BLING_CHANNEL);
	}
	
	public void advanceToNextMode() {
		bling.pulse(RobotMap.BLING_CHANNEL, (float) .5);
	}

	protected void initDefaultCommand() {
	}
}

