
package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.Robot;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivechain extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private RobotDrive drive;
	
	
	public Drivechain() {
		
		drive = new RobotDrive(new CANJaguar(RobotMap.FRONT_LEFT_MOTOR), new CANJaguar(RobotMap.REAR_LEFT_MOTOR), new CANJaguar(RobotMap.FRONT_RIGHT_MOTOR), new CANJaguar(RobotMap.REAR_RIGHT_MOTOR));
	}
	
	public void fieldOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getRotateStick().getY();
		double rotate = OI.getInstance().getTranslateStick().getX();
		double angle = Robot.sensors.getAngle();
		drive.mecanumDrive_Cartesian(transX, transY, rotate, angle);
	}
	
	
	
	public void teleoperatedDrive() {
		fieldOrientedDrive();
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

