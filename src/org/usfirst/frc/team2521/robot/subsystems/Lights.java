package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lights extends Subsystem {
	DigitalOutput bling;
	
	public Lights() {
		bling = new DigitalOutput(RobotMap.BLING_CHANNEL);
	}
	
	public void advanceToNextMode() {
		bling.pulse(RobotMap.BLING_CHANNEL, .5f);
	}

	protected void initDefaultCommand() {
	}
}

