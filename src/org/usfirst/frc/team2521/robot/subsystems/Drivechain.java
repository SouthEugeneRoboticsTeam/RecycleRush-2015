
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.TeleoperatedDrive;



//import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivechain extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private RobotDrive drive;
	private Gyro gyro;
	private DriveMode mode = DriveMode.robotOrientedMecanum;
	boolean slowMode = false;
	
	CANTalon frontLeft, frontRight, rearLeft, rearRight;
	
	public Drivechain() {
		frontLeft = new CANTalon(RobotMap.FRONT_LEFT_MOTOR);
		frontLeft.changeControlMode(ControlMode.PercentVbus);
		frontLeft.enableBrakeMode(true);
		frontLeft.enableControl();
		
		rearLeft = new CANTalon(RobotMap.REAR_LEFT_MOTOR);
		rearLeft.changeControlMode(ControlMode.PercentVbus);
		rearLeft.enableBrakeMode(true);
		rearLeft.enableControl();
		
		frontRight = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR);
		frontRight.changeControlMode(ControlMode.PercentVbus);
		frontRight.enableBrakeMode(true);
		frontRight.enableControl();
		
		rearRight = new CANTalon(RobotMap.REAR_RIGHT_MOTOR);
		rearRight.changeControlMode(ControlMode.PercentVbus);
		rearRight.enableBrakeMode(true);
		rearRight.enableControl();

		drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
		drive.setInvertedMotor(MotorType.kFrontLeft, true);
		drive.setInvertedMotor(MotorType.kRearLeft, true);
		
		gyro = new Gyro(RobotMap.GYRO_PORT);
	}
	
	public void toggleSlowMode(boolean set) {
		slowMode = set;
	}
	
	public void fieldOrientedDrive() { 
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getTranslateStick().getY();
		double rotation = OI.getInstance().getRotateStick().getX();
		if (slowMode) {
			rotation = rotation*.4;
		}
		double angle = getAngle();
		drive.mecanumDrive_Cartesian(transX, transY, rotation, angle);
	}
	
	public void robotOrientedDrive() {
		double rotation = OI.getInstance().getRotateStick().getX();
		if (slowMode) {
			rotation = rotation*.4;
		}
		double magnitude = OI.getInstance().getTranslateStick().getMagnitude();
		double direction = OI.getInstance().getTranslateStick().getDirectionDegrees();
		drive.mecanumDrive_Polar(magnitude, direction, rotation);	
	}
	
	public void arcadeDrive() {
		double rotation = OI.getInstance().getTranslateStick().getX();
		double magnitude = OI.getInstance().getTranslateStick().getY();
		drive.arcadeDrive(magnitude, rotation);
	}
	
	public void tankDrive() {
		double left = OI.getInstance().getTranslateStick().getY();
		double right = OI.getInstance().getRotateStick().getY();
		drive.tankDrive(left, right);
	}
	
	
	public void invertLeftDrive(boolean invert) {
		drive.setInvertedMotor(MotorType.kFrontLeft, invert);
		drive.setInvertedMotor(MotorType.kRearLeft, invert);
	}
	
	public double getAngle(){
		return gyro.getAngle();
	}

	
	public void resetGyro() {
		gyro.reset();
	}	
	
	public void teleoperatedDrive() {
			switch (mode) {
			case fieldOrientedMecanum:
				invertLeftDrive(true);
				fieldOrientedDrive();
				break;
			case robotOrientedMecanum:
				invertLeftDrive(true);
				robotOrientedDrive();
				break;
			case arcadeDrive:
				invertLeftDrive(false);
				arcadeDrive();
				break;
			case tankDrive:
				invertLeftDrive(false);
				tankDrive();
				break;
			}
	}
	
	public void polarDrive(double magnitude, double direction, double rotation) {
		drive.mecanumDrive_Polar(magnitude, direction, rotation);
	}
	
	public void switchDriveMode(DriveMode newMode) {
		mode = newMode;
		SmartDashboard.putString("Current Drive Mode", mode.identifier);
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new TeleoperatedDrive());
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
    	


