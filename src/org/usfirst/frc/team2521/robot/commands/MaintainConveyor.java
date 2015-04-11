package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MaintainConveyor extends Command {
	
	int position;
	boolean newPosition = false;
	
    public MaintainConveyor() {
    	requires(Robot.conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (!newPosition) {
    		position = Robot.conveyor.getPosition();
    		newPosition = true;
        	Robot.conveyor.setPID(RobotMap.MAINTAIN_P,RobotMap.MAINTAIN_I, RobotMap.MAINTAIN_D);
    	}
    	Robot.conveyor.setPosition(position);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	newPosition = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
