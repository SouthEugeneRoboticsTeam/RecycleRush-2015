package team2521.billy;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Ports {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	public static final int FRONT_RIGHT_MOTOR = 0;
	public static final int FRONT_LEFT_MOTOR = 0;
	public static final int REAR_RIGHT_MOTOR = 0;
	public static final int REAR_LEFT_MOTOR = 0;
	public static final int TRANSLATE_PORT = 0;
	public static final int ROTATE_PORT = 1;
	
}
