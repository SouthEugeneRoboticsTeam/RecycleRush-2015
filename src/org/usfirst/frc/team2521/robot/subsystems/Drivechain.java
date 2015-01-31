
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.commands.TeleoperatedDrive;


//import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.List;

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
	String RobotOrientedRotation = "/home/lvuser/auto/RobotOrientedRotation.txt";
	String Magnitude = "/home/lvuser/auto/Magnitude.txt";
	String Direction = "/home/lvuser/auto/Direction.txt";
	String FieldOrientedRotation = "/home/lvuser/auto/FieldOrientedRotation.txt";
	String TransX = "/home/lvuser/auto/TransX.txt";
	String TransY = "/home/lvuser/auto/TransY.txt";
	String Angle = "/home/lvuser/auto/Angle.txt";
	//String autoFieldOriented = "/tmp/autoFieldOriented.txt";
	BufferedWriter ROrotWriter = null;
	BufferedWriter magWriter = null;
	BufferedWriter dirWriter = null;
	BufferedWriter FOrotWriter = null;
	BufferedWriter transXWriter = null;
	BufferedWriter transYWriter = null;
	BufferedWriter logWriter = null;
	//BufferedWriter angleWriter = null;
	String path;
	
	CANTalon frontLeft, frontRight, rearLeft, rearRight;
	
	public Drivechain() {
		frontLeft = new CANTalon(RobotMap.FRONT_LEFT_MOTOR);
		frontLeft.changeControlMode(ControlMode.PercentVbus);
		//frontLeft.setPercentMode(CanTalonSRX.kFeedbackDev_AnalogEncoder, 360);
		frontLeft.enableControl();
		rearLeft = new CANTalon(RobotMap.REAR_LEFT_MOTOR);
		rearLeft.changeControlMode(ControlMode.PercentVbus);
		rearLeft.enableControl();
		frontRight = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR);
		frontRight.changeControlMode(ControlMode.PercentVbus);
		frontRight.enableControl();
		rearRight = new CANTalon(RobotMap.REAR_RIGHT_MOTOR);
		rearRight.changeControlMode(ControlMode.PercentVbus);
		rearRight.enableControl();
		drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
		drive.setInvertedMotor(MotorType.kFrontLeft, true);
		drive.setInvertedMotor(MotorType.kRearLeft, true);
//		drive = new RobotDrive(0, 1, 2, 3);
	}
	
	
	public void driveLog(){
		Robot.fileManager.createLog("/home/lvuser/data/joysticks_", Timer.getFPGATimestamp() + "," + 
				mode + "," +
				frontLeft.getOutputVoltage() + "," +
				frontRight.getOutputVoltage() + "," +
				rearRight.getOutputVoltage() + "," +
				rearLeft.getOutputVoltage() + "," +
				frontLeft.get() + "," +
				frontRight.get() + "," +
				rearRight.get() + "," +
				rearLeft.get() + "," +
				frontLeft.getOutputCurrent() + "," +
				frontRight.getOutputCurrent() + "," +
				rearRight.getOutputCurrent() + "," +
				rearLeft.getOutputCurrent() + ",");
	}
	
	public void fieldOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getTranslateStick().getY();
		double rotation = OI.getInstance().getRotateStick().getX();
		double angle = Robot.sensors.getAngleX();
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
	
	public void autoInit(){
		switch (mode) {
		case fieldOrientedMecanum:
			transX = Robot.fileManager.fileToArray(TransX, TOTAL_EXECUTIONS);
			transY = Robot.fileManager.fileToArray(TransY, TOTAL_EXECUTIONS);
			fieldOrientedRotation = Robot.fileManager.fileToArray(FieldOrientedRotation, TOTAL_EXECUTIONS);
			break;
		case robotOrientedMecanum:
			robotOrientedRotation = Robot.fileManager.fileToArray(RobotOrientedRotation, TOTAL_EXECUTIONS);
			magnitude = Robot.fileManager.fileToArray(Magnitude, TOTAL_EXECUTIONS);
			direction = Robot.fileManager.fileToArray(Direction, TOTAL_EXECUTIONS);
			break;
		}
	}
	
	public void auto(){
		switch (mode) {
		case fieldOrientedMecanum:
			autoFieldOrientedRemembering();
			break;
		case robotOrientedMecanum:
			autoRobotOrientedRemembering();
			break;
		}
	}

	public void autoRobotOrientedRemembering() {
		if (autoCounter <= TOTAL_EXECUTIONS) {
			drive.mecanumDrive_Polar(magnitude[teleopCounter], direction[teleopCounter], robotOrientedRotation[teleopCounter]);
			autoCounter++;
		}
	}
	
	public void autoFieldOrientedRemembering(){
		if (autoCounter <= TOTAL_EXECUTIONS) {
			drive.mecanumDrive_Cartesian(transX[autoCounter], transY[autoCounter], fieldOrientedRotation[autoCounter], Robot.sensors.getAngleX());
			autoCounter++;
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
				writeToFileFieldOriented();
				break;
			case robotOrientedMecanum:
				invertLeftDrive(true);
				robotOrientedDrive();
				writeToFileRobotOriented();
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
	
	public void fromFileSetUp(){
		switch (mode) {
		case fieldOrientedMecanum:
			writeToFileFieldOrientedSetUp();
			break;
		case robotOrientedMecanum:
			writeToFileRobotOrientedSetUp();
			break;
		}
			
	}
	
	public void writeToFileRobotOrientedSetUp() {
		if (ROrotWriter == null || magWriter == null || dirWriter == null ) {
			File ROrotationFile = new File(RobotOrientedRotation);
			File magnitudeFile = new File(Magnitude);
			File directionFile = new File(Direction);
			if (!ROrotationFile.exists() || !magnitudeFile.exists() || !directionFile.exists()) {
				try {
					ROrotationFile.createNewFile();
				} catch (IOException e) {}
				try {
					magnitudeFile.createNewFile();
				} catch (IOException e) {}
				try {
					directionFile.createNewFile();
				} catch (IOException e) {}
			}
	    	try {
				ROrotWriter = new BufferedWriter(new FileWriter(ROrotationFile));
				magWriter = new BufferedWriter(new FileWriter(magnitudeFile));
				dirWriter = new BufferedWriter(new FileWriter(directionFile));
			} catch (IOException e) {
				
			} 
		}
	}
	
	/*public void recordFromFileRobotOriented() {
		angle = 
	}*/
	
	public void writeToFileRobotOriented() {
		try {
			if (ROrotWriter != null) {
			ROrotWriter.write(OI.getInstance().getRotateStick().getX() + "\n");
			ROrotWriter.flush();
			}
		} catch (IOException ex) {}
		try {
			if (magWriter != null) {
			magWriter.write(OI.getInstance().getTranslateStick().getMagnitude() + "\n");
			magWriter.flush();
			}
		} catch (IOException ex) {}
		try {
			if (dirWriter != null) {
			dirWriter.write(OI.getInstance().getTranslateStick().getDirectionDegrees() + "\n");
			dirWriter.flush();
			}
		} catch (IOException ex) {}
	}  
	
	public void writeToFileFieldOrientedSetUp() {
		if (FOrotWriter == null || transXWriter == null || transYWriter == null) {
			File FOrotationFile = new File(FieldOrientedRotation);
			File transXFile = new File(TransX);
			File transYFile = new File(TransY);
			//File angleFile = new File(Angle);
			if (!FOrotationFile.exists() || !transXFile.exists() || !transYFile.exists()) {
				try {
					FOrotationFile.createNewFile();
				} catch (IOException e) {}
				try {
					transXFile.createNewFile();
				} catch (IOException e) {}
				try {
					transYFile.createNewFile();
				} catch (IOException e) {}
				/*try {
					angleFile.createNewFile();
				} catch (IOException e) {} */
			}
	    	try {
				FOrotWriter = new BufferedWriter(new FileWriter(FOrotationFile));
				transXWriter = new BufferedWriter(new FileWriter(transXFile));
				transYWriter = new BufferedWriter(new FileWriter(transYFile));
				//angleWriter = new BufferedWriter(new FileWriter(angleFile));
			} catch (IOException e) {
				
			} 
		}
	}
	
	public void writeToFileFieldOriented() {
		try {
			if (FOrotWriter != null) {
			FOrotWriter.write(OI.getInstance().getRotateStick().getX() + "\n");
			FOrotWriter.flush();
			}
		} catch (IOException ex) {}
		try {
			if (transXWriter != null) {
			transXWriter.write(OI.getInstance().getTranslateStick().getX() + "\n");
			transXWriter.flush();
			}
		} catch (IOException ex) {}
		try {
			if (transYWriter != null) {
			transYWriter.write(OI.getInstance().getRotateStick().getX() + "\n");
			transYWriter.flush();
			}
		} catch (IOException ex) {}
		/*try {
			if (angleWriter != null) {
			angleWriter.write(OI.getInstance().getTranslateStick().getDirectionDegrees() + "\n");
			angleWriter.flush(); 
			}
		} catch (IOException ex) {}*/
	} 

	
	public void auto1() {
		drive.mecanumDrive_Cartesian(.1, .1, 0, Robot.sensors.getAngleX());
	}
	
	public void auto2() {
		drive.mecanumDrive_Cartesian(.5, .5, 0, Robot.sensors.getAngleX());
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
			double angle = Robot.sensors.getAngleX();
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
    	


