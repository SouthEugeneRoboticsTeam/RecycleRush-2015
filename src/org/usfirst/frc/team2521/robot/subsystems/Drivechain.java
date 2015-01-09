
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.Robot;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivechain extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private RobotDrive drive;
	private DriveMode mode = DriveMode.fieldOrientedMecanum;
	
	public Drivechain() {
		CANJaguar frontLeft = new CANJaguar(RobotMap.FRONT_LEFT_MOTOR);
		CANJaguar rearLeft = new CANJaguar(RobotMap.REAR_LEFT_MOTOR);
		CANJaguar frontRight = new CANJaguar(RobotMap.FRONT_RIGHT_MOTOR);
		CANJaguar rearRight = new CANJaguar(RobotMap.REAR_RIGHT_MOTOR);
		drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	}
	
	public void fieldOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getRotateStick().getY();
		double rotation = OI.getInstance().getTranslateStick().getX();
		double angle = Robot.sensors.getAngle();
		drive.mecanumDrive_Cartesian(transX, transY, rotation, angle);
	}
	
	public void robotOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getRotateStick().getY();
		double rotation = OI.getInstance().getTranslateStick().getX();
		double magnitude = Math.sqrt(Math.pow(transX, 2) + Math.pow(transY, 2)); // Pythagorean theorem for the hypotenuse
		double direction = 0;
		if (transY == 0) {
			direction = Math.PI/2;
		}	
		drive.mecanumDrive_Polar(magnitude, direction, rotation);	
	}
	
	public void arcadeDrive() {
		drive.arcadeDrive(OI.getInstance().getTranslateStick());
	}
	
	public void tankDrive() {
		drive.tankDrive(OI.getInstance().getTranslateStick(), OI.getInstance().getRotateStick());
	}
	
	public void teleoperatedDrive() {
		switch (mode) {
		case fieldOrientedMecanum:
			fieldOrientedDrive();
			break;
		case robotOrientedMecanum:
			robotOrientedDrive();
			break;
		case arcadeDrive:
			arcadeDrive();
			break;
		case tankDrive:
			tankDrive();
			break;
		}
	}
	
	public void switchDriveMode(DriveMode newMode) {
		mode = newMode;
		SmartDashboard.putString("Current Drive Mode", mode.identifier);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public enum DriveMode {
    	fieldOrientedMecanum ("Field Oriented Drive"),
    	robotOrientedMecanum ("Robot Oriented Drive"),
    	arcadeDrive ("Arcade Drive"),
    	tankDrive ("Tank Drive");
    	
    	private final String identifier;
    	private DriveMode(String identifier) {
    		this.identifier = identifier;
    	}
    }
}

