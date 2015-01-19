
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.commands.TeleoperatedDrive;

import edu.wpi.first.wpilibj.CANJaguar;
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
	private DriveMode mode = DriveMode.fieldOrientedMecanum;
	
	final int TOTAL_EXECUTIONS = 9000; //how many times the function should execute in 3 minutes
	double[] transX = new double[TOTAL_EXECUTIONS];
	double[] transY = new double[TOTAL_EXECUTIONS];
	double[] fieldOrientedRotation = new double[TOTAL_EXECUTIONS];
	double[] robotOrientedRotation = new double[TOTAL_EXECUTIONS];
	double[] angle = new double[TOTAL_EXECUTIONS];
	double[] magnitude = new double[TOTAL_EXECUTIONS];
	double[] direction = new double[TOTAL_EXECUTIONS];
	int teleopCounter = 0;
	int autoCounter = 0;
	boolean isRemembering = false;
	
	CANJaguar frontLeft, frontRight, rearLeft, rearRight;
	
	public Drivechain() {
		frontLeft = new CANJaguar(RobotMap.FRONT_LEFT_MOTOR);
		frontLeft.setPercentMode(CANJaguar.kQuadEncoder, 360);
		frontLeft.enableControl();
		rearLeft = new CANJaguar(RobotMap.REAR_LEFT_MOTOR);
		rearLeft.setPercentMode();
		rearLeft.enableControl();
		frontRight = new CANJaguar(RobotMap.FRONT_RIGHT_MOTOR);
		frontRight.setPercentMode(CANJaguar.kQuadEncoder, 360);
		frontRight.enableControl();
		rearRight = new CANJaguar(RobotMap.REAR_RIGHT_MOTOR);
		rearRight.setPercentMode();
		rearRight.enableControl();
		drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
		drive.setInvertedMotor(MotorType.kFrontLeft, true);
		drive.setInvertedMotor(MotorType.kRearLeft, true);
//		drive = new RobotDrive(0, 1, 2, 3);
	}
	
	public void fieldOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getTranslateStick().getY();
		double rotation = OI.getInstance().getRotateStick().getX();
		double angle = Robot.sensors.getAngle();
		drive.mecanumDrive_Cartesian(transX, transY, rotation, angle);
	}
	
	public void robotOrientedDrive() {
		double rotation = OI.getInstance().getRotateStick().getX();
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
	
	public void robotOrientedDriveRemembering() {
		if (teleopCounter <= TOTAL_EXECUTIONS) {
		robotOrientedRotation[teleopCounter] = OI.getInstance().getRotateStick().getX();
		magnitude[teleopCounter] = OI.getInstance().getTranslateStick().getMagnitude();
		direction[teleopCounter] = OI.getInstance().getTranslateStick().getDirectionDegrees();
		drive.mecanumDrive_Polar(magnitude[teleopCounter], direction[teleopCounter], robotOrientedRotation[teleopCounter]);
		teleopCounter++;
		}
	}
	
	
	public void invertLeftDrive(boolean invert) {
		drive.setInvertedMotor(MotorType.kFrontLeft, invert);
		drive.setInvertedMotor(MotorType.kRearLeft, invert);
	}
	
	public void teleoperatedDrive() {
		if (!isRemembering) {
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
		} else {
			switch (mode) {
			case fieldOrientedMecanum:
				invertLeftDrive(true);
				fieldOrientedDriveRemembering();
				break;
			case robotOrientedMecanum:
				invertLeftDrive(true);
				robotOrientedDriveRemembering();
				break;
			}
		}
	}
	
	
	public void toggleRemembering() {
		isRemembering  = !isRemembering;
	}
	
	public void switchDriveMode(DriveMode newMode) {
		mode = newMode;
		SmartDashboard.putString("Current Drive Mode", mode.identifier);
	}
	  
	public void autoRemembering() {
		
	}
	
	public void auto1() {
		drive.mecanumDrive_Cartesian(.1, .1, 0, Robot.sensors.getAngle());
	}
	
	public void auto2() {
		drive.mecanumDrive_Cartesian(.5, .5, 0, Robot.sensors.getAngle());
	}
	
	public void resetRemembering() {
		transX = new double[TOTAL_EXECUTIONS];
		transY = new double[TOTAL_EXECUTIONS];
		fieldOrientedRotation = new double[TOTAL_EXECUTIONS];
		robotOrientedRotation= new double[TOTAL_EXECUTIONS];
		angle = new double[TOTAL_EXECUTIONS];
		magnitude = new double[TOTAL_EXECUTIONS];
		direction = new double[TOTAL_EXECUTIONS];
		teleopCounter = 0;
		autoCounter = 0;
	}
	public void fieldOrientedDriveRemembering() {
		if (teleopCounter <= TOTAL_EXECUTIONS) {
			transX[teleopCounter] = OI.getInstance().getTranslateStick().getX();
			transY[teleopCounter] = OI.getInstance().getTranslateStick().getY();
			fieldOrientedRotation[teleopCounter] = OI.getInstance().getRotateStick().getX();
			//angle[teleopCounter] = Robot.sensors.getAngle();
			double angle = Robot.sensors.getAngle();
			drive.mecanumDrive_Cartesian(transX[teleopCounter], transY[teleopCounter], fieldOrientedRotation[teleopCounter], angle);
			teleopCounter++;
		}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
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
    
   /* public enum AutoMode {
    	auto1 ("First autonomous mode"),
    	auto2 ("Second autonomous mode");
    	
    	private final String identifier;
    	private AutoMode(String identifier) {
    		this.identifier = identifier;
    	}
    }
    */
}
    	


