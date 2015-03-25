package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SwitchDriveMode extends Command {

	DriveMode mode;
	
    public SwitchDriveMode(DriveMode mode) {
    	this.mode = mode;
    	requires(Robot.drivechain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivechain.switchDriveMode(mode);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
