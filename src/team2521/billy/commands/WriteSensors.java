package team2521.billy.commands;

import team2521.billy.Billy;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class WriteSensors extends Command {

    public WriteSensors() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Billy.sensors);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("Angle", Billy.sensors.getAngle());
    	SmartDashboard.putNumber("Ultrasonic Raw", Billy.sensors.getUltrasonicVoltage());
    	SmartDashboard.putNumber("Ultrasonic Distance", Billy.sensors.getUltrasonicDistance());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
