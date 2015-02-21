package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveToteUp extends Command {

    public MoveToteUp() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	int position = Robot.conveyor.getPosition();
    	boolean deltaPosition = position % RobotMap.CODES_PER_HOOK < 500;
    	int setPosition;
//    	if (deltaPosition) {
//        	setPosition = position + (position % RobotMap.CODES_PER_HOOK) - RobotMap.CODES_PER_HOOK + RobotMap.TOTE_PICKUP_OFFSET;
//    	} else {
//        	setPosition = position - (position % RobotMap.CODES_PER_HOOK) + RobotMap.TOTE_PICKUP_OFFSET;
//    	}
    	setPosition = position + (position % RobotMap.CODES_PER_HOOK);
    	Robot.conveyor.setPosition(setPosition);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
