package org.usfirst.frc.team2521.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TeleopReset extends CommandGroup {
    
    public  TeleopReset() {
    	addSequential(new PolarDrive(0, 0, 0),.01);
    	addSequential(new MoveConveyor(0),.01);
    }
}
