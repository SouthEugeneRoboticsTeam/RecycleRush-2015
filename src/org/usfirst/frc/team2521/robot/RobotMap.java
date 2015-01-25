package org.usfirst.frc.team2521.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	public static final int FRONT_RIGHT_MOTOR = 44;
	public static final int FRONT_LEFT_MOTOR = 42;
	public static final int REAR_RIGHT_MOTOR = 41;
	public static final int REAR_LEFT_MOTOR = 43;
	public static final int GYRO_PORT = 0;
	public static final int EMPTY_ANALOG = 3;
	public static final int ULTRASONIC_PORT = 1;
	public static final int TRANSLATE_PORT = 0;
	public static final int ROTATE_PORT = 1;
	public static final int CONVEYOR_MOTOR = 32;
	public static final int FLIPPER_UP_CHANNEL = 1;
	public static final int FLIPPER_DOWN_CHANNEL = 0;
	public static final int LIMIT_SWITCH_PORT_TOP = 0; 
	public static final int LIMIT_SWITCH_PORT_BOT = 1; 
	public static final int LIGHT_MAIN = 2; 
	public static final int LIGHT_1 = 3; 
	public static final int LIGHT_2 = 4; 
	public static final int LIGHT_3 = 5; 
	public static final int LIGHT_4 = 6; 
	public static final int LIGHT_5 = 7; 
	public static final int LIGHT_6 = 8; 
	public static final int LIGHT_7 = 9; 
	public static final double CONVEYOR_SPEED = .2;
	public static final double LOW_PASS_ALPHA = .5;
	public static final int PCM_CAN_CHANNEL = 9;
}
