package org.usfirst.frc.team2521.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {	
	public static final int FRONT_RIGHT_MOTOR = 51;
	public static final int FRONT_LEFT_MOTOR = 53;
	public static final int REAR_RIGHT_MOTOR = 54;
	public static final int REAR_LEFT_MOTOR = 50;
	public static final int CONVEYOR_MASTER = 55;
	public static final int CONVEYOR_SLAVE = 56;
	
	public static final int GYRO_PORT = 0;
	public static final int MAG_READ_CHANNEL = 2;
	public static final int BLING_CHANNEL = 1;
	
	public static final int TRANSLATE_PORT = 0;
	public static final int ROTATE_PORT = 1;
	public static final int CUSTOM_PORT = 2;
	
	public static final double CONVEYOR_SPEED_HI = .5; // conveyor speed when moving up
	public static final double CONVEYOR_SPEED_LO = .7; // conveyor speed when moving down

	public static final int[] BIN_PICKUPS = {1766, 21754};
	public static final int[] BIN_STEP_PICKUPS = {53, 20140};
	public static final int[] HOOK_POSITIONS = {364, -1962, -4257, -6631,
												-9107, -11484, -13873, -15991,
												-18342, -20482, -22767, -24991,
												-27185, -29406, -31757, -33962,
												-36175, -38473};
}
