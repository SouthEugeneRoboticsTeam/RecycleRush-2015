
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.TeleoperatedDrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drivechain extends Subsystem implements PIDOutput {
    
    // Put methods for controlling this subsystem here.
    // Call these from Commands.
	
	private RobotDrive drive;
	private AHRS ahrs;
	private DriveMode mode = DriveMode.robotOrientedMecanum;
	boolean slowMode = false;
    PIDController turnController;
    double rotateToAngleRate;
	double last_world_linear_accel_x = 0;
	double last_world_linear_accel_y = 0;
	public static boolean collisionDetected = false;
	double gyroAngle = getAngle();
	
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
		
        try {
            // Communicate w/navX-MXP via the MXP SPI Bus.
            ahrs = new AHRS(SPI.Port.kMXP); 
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
        
        turnController = new PIDController(RobotMap.kP, RobotMap.kI, RobotMap.kD, RobotMap.kF, ahrs, this);
        turnController.setInputRange(-180.0f,  180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(RobotMap.kToleranceDegrees);
        turnController.setContinuous(true);
        LiveWindow.addActuator("DriveSystem", "RotateController", turnController);
		
        double curr_world_linear_accel_x = ahrs.getWorldLinearAccelX();
        double currentJerkX = curr_world_linear_accel_x - last_world_linear_accel_x;
        last_world_linear_accel_x = curr_world_linear_accel_x;
        double curr_world_linear_accel_y = ahrs.getWorldLinearAccelY();
        double currentJerkY = curr_world_linear_accel_y - last_world_linear_accel_y;
        last_world_linear_accel_y = curr_world_linear_accel_y;
        
        if ( ( Math.abs(currentJerkX) > RobotMap.kCollisionThreshold_DeltaG ) ||
             ( Math.abs(currentJerkY) > RobotMap.kCollisionThreshold_DeltaG ) ) {
            collisionDetected = true;
        }
        SmartDashboard.putBoolean( "CollisionDetected", collisionDetected );
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
		
        try {
        	drive.mecanumDrive_Cartesian(transX, transY, rotation, gyroAngle);
        } catch( RuntimeException ex ) {
            String err_string = "Drive system error:  " + ex.getMessage();
            DriverStation.reportError(err_string, true);
        }
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
		return ahrs.getAngle();
	}

	
	public void resetGyro() {
		ahrs.reset();
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
	
	public void navXAuto (double x, double y, double customAngle) {
		double rotation;
		turnController.setSetpoint(customAngle);
		turnController.enable();
		rotation = rotateToAngleRate;
		try {
			drive.mecanumDrive_Cartesian(x, y, rotation, getAngle());
        } catch( RuntimeException ex ) {
            String err_string = "Drive system error:  " + ex.getMessage();
            DriverStation.reportError(err_string, true);
        }
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

    @Override
    /* This function is invoked periodically by the PID Controller, */
    /* based upon navX MXP yaw angle input and PID Coefficients.    */
    public void pidWrite(double output) {
        rotateToAngleRate = output;
    }
}
    	


