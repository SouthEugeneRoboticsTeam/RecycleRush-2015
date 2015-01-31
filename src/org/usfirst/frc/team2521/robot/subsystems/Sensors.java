package org.usfirst.frc.team2521.robot.subsystems;



import org.usfirst.frc.team2521.robot.ComplementaryFilter;
import org.usfirst.frc.team2521.robot.DataBasedFilter;
import org.usfirst.frc.team2521.robot.LowPassFilter;
import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.Robot;
import org.usfirst.frc.team2521.robot.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.ADXL345_I2C;

/**
 *
 */
public class Sensors extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private Gyro gyro;
	private AnalogInput ultrasonic;
	private BuiltInAccelerometer accel1;
	private ADXL345_I2C accel2;
	private AnalogInput blankspace;
	private double smoothedDistance = 0;
	private ComplementaryFilter compFilter;
	private DataBasedFilter dbFilter;
	private LowPassFilter lpFilter;
	private GyroITG3200 m_gyro;
	
	

	
	
	public Sensors() {
		gyro = new Gyro(RobotMap.GYRO_PORT);
		blankspace = new AnalogInput(RobotMap.EMPTY_ANALOG);
		ultrasonic = new AnalogInput(RobotMap.ULTRASONIC_PORT);
		accel1 = new BuiltInAccelerometer();
		accel2 = new ADXL345_I2C(I2C.Port.kOnboard, Accelerometer.Range.k16G);
		compFilter = new ComplementaryFilter(gyro, accel1, 1);
		dbFilter = new DataBasedFilter(gyro, accel1);
		lpFilter = new LowPassFilter(gyro);
		m_gyro = new GyroITG3200(I2C.Port.kOnboard);
		m_gyro.initialize();
		
		

	}
	
	
	
	public void gyroWriter() {
		
	}
	
	public double getAngleX() {
		//return gyro.getAngle();
		return m_gyro.getRotationX();
	}
	
	public double getAngleY() {
		//return gyro.getAngle();
		return m_gyro.getRotationY();
	}
	
	public double getAngleZ() {
		//return gyro.getAngle();
		return m_gyro.getRotationZ();
	}
	
	public double getUltrasonicVoltage() {
		return ultrasonic.getVoltage();
	}
	
	public double getUltrasonicDistance() {
		double distance = ultrasonic.getVoltage()*(8.503401361); // Unit conversion 9.8mV/in=8.503ft/V
		return distance;
	}
	
	private double lowPass(double newDistance) {
		return smoothedDistance += (newDistance - smoothedDistance) / RobotMap.LOW_PASS_ALPHA;
	}
	
	public double getSmoothedUltrasonicDistance() {
		lowPass(getUltrasonicVoltage());
		return smoothedDistance;
	}
	
	public double getComplementaryAngle() {
		return compFilter.getAngle();
	}
	
	public double getDataBasedAngle() {
		return dbFilter.getAngle();
	}
	
	public double getDBFilterError() {
		return dbFilter.getError();
	}
	
	public double getLPAngle(){
		return lpFilter.getAngle();
	}
	
	public void resetGyro() {
		//gyro.reset();
		m_gyro.reset();
	}
	
	public double getBlank(){
		return blankspace.getValue();
	}
	
	public double getNewAccelX(){
		return accel2.getX();
	}
	
	public double getNewAccelY(){
		return accel2.getY();
	}
	
	public double getNewAccelZ(){
		return accel2.getZ();
	}
	
	public void commandLog(){
		Robot.fileManager.createLog("/home/lvuser/data/command_", Timer.getFPGATimestamp() + "," + 
				Robot.compressor.getCurrentCommand() + "," +
				Robot.conveyor.getCurrentCommand() + "," +
				Robot.drivechain.getCurrentCommand() + "," +
				Robot.flipper.getCurrentCommand() + "," + "\n");
		}
	
	public void sensorLog(){
		Robot.fileManager.createLog("/home/lvuser/data/sensor_", Timer.getFPGATimestamp() + "," + 
				ultrasonic.getVoltage() + "," +
				accel2.getX() +  "," + 
				accel2.getY() + "," +
				accel2.getZ() + "," +
				m_gyro.getRotationX() + "," +
				m_gyro.getRotationY() + "," +
				m_gyro.getRotationZ() + "," +
				blankspace.getValue() + "\n");
	}
	
	public void joystickLog(){
		Robot.fileManager.createLog("/home/lvuser/data/joysticks_", Timer.getFPGATimestamp() + "," + 
				OI.getInstance().getTranslateStick().getX() + "," +
				OI.getInstance().getTranslateStick().getY() + "," +
				OI.getInstance().getRotateStick().getX() + "," +
				OI.getInstance().getTranslateStick().getMagnitude() + "," +
				OI.getInstance().getTranslateStick().getDirectionDegrees() + "\n");
	}
	
	
	
    public void initDefaultCommand() {
    	setDefaultCommand(new WriteSensors());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

	// This is a very simple weighted average filter
	public double GetSimpleFilter( double RA[] )
	{
		double result = 0;
		
		int len = RA.length;
		
		double val_n = RA[ len - 1 ];
		double val_n1 = RA[ len - 2 ];
		double val_n2 = RA[ len - 3 ];
		double val_n3 = RA[ len - 4 ];
		double weightN = 5;
		double weightN1 = 4;
		double weightN2 = 3;
		double weightN3 = 1;
		
		double sum = val_n * weightN;
		sum += val_n1 * weightN1;
		sum += val_n2 * weightN2;
		sum += val_n3 * weightN3;
		
		result = sum / ( weightN + weightN1 + weightN2 + weightN3 );
		
		return result;
	}
}

