package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveConveyor extends Command {
	double speed;
	static boolean UpperlimitReached = false;
    public MoveConveyor(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.conveyor);
    	this.speed = speed;
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*speed = SmartDashboard.getNumber("Conveyor speed");*/
    	if (speed > 0){
    		if (!UpperlimitReached){
    			SmartDashboard.putBoolean("Limit switch:", Robot.sensors.getLimitSwitchTop());
    		}
    	} else Robot.conveyor.moveConveyor(speed);
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//return false;
        /*if (speed < 0){
        	return Robot.sensors.getLimitSwitchBot();
        } else */
        if (speed > 0) {
        	UpperlimitReached = !Robot.sensors.getLimitSwitchTop();
        	return UpperlimitReached;
        } else
        	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.conveyor.moveConveyor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
