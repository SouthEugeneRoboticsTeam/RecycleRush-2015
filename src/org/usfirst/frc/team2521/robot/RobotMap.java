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
	
	public static final double FREE_P = 5;
	public static final double FREE_I = 0;
	public static final double FREE_D = 2;
	
	public static final double SLOW_P = 2.5;
	public static final double SLOW_I = 0;
	public static final double SLOW_D = .8;
		
	public static final double MAINTAIN_P = 1;
	public static final double MAINTAIN_I = 0;
	public static final double MAINTAIN_D = 0;

	public static final int CODES_PER_CYCLE = 40928;
	public static final int[] BIN_PICKUPS = {-39162, -19174};     	//, 1766, 21754};
	public static final int[] BIN_STEP_PICKUPS = {-40875, -20788}; 	//, 53, 20140};
	public static final int[] HOOK_POSITIONS = {-40564, -38473, -36175, -33962,
												-31757,	-29406, -27185, -24991, 
												-22767,	-20482, -18342, -15991,
												-13873,	-11484, -9107, -6631, 
												-4257, -1962};
//												364, 2455, 
//												4753, 6966, 9171, 11522,
//												13743, 15937, 18161, 20446,
//												22586, 24937, 27055, 29444,
//												31821, 34297, 36671, 38966};
}
