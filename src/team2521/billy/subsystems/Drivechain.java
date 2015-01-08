
package team2521.billy.subsystems;

import team2521.billy.Billy;
import team2521.billy.OI;
import team2521.billy.Ports;
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
		drive = new RobotDrive(Ports.FRONT_LEFT_MOTOR, Ports.REAR_LEFT_MOTOR, Ports.FRONT_RIGHT_MOTOR, Ports.REAR_RIGHT_MOTOR);
	}
	
	public void fieldOrientedDrive() {
		double transX = OI.getInstance().getTranslateStick().getX();
		double transY = OI.getInstance().getRotateStick().getY();
		double rotate = OI.getInstance().getTranslateStick().getX();
		double angle = Billy.sensors.getAngle();
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

