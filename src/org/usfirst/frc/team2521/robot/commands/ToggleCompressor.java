package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToggleCompressor extends Command {
	private static boolean isOn = false;
	
    public ToggleCompressor() {
    	//requires(Robot.compressor);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.flipper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.compressor.startCompressor();
    	//Robot.compressor.compLog();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.flipper.setClosedLoopControl(!isOn);
    	isOn= !isOn;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
