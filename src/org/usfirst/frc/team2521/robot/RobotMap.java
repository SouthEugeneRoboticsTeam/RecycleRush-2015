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
	
	public static final double SLOW_P = .4;
	public static final double SLOW_I = 0;
	public static final double SLOW_D = .1;
		
	public static final double MAINTAIN_P = .5;
	public static final double MAINTAIN_I = 0;
	public static final double MAINTAIN_D = 0;

	public static final int CODES_PER_CYCLE = 40687;
	public static final int HORIZONTAL_BIN_OFFSET = 436;
	public static final int[] BIN_PICKUPS = {-22125, -1951, 18314, 38378};     	//, 1766, 21754};
	public static final int[] BIN_STEP_PICKUPS = {-20279, -168, 20150, 40519}; 	//, 53, 20140};
	public static final int[] HOOK_POSITIONS = {-40912, -38899, -36506, -34213, -31883,
												-29623, -27266, -25115, -22841,
												-20595, -18387, -16147, -13837,
												-11783, -9391, -7104, -4893,
												-310, 1910, 4163, 6540,
												8765, 11119, 13422, 15589,
												17914, 20073, 22372, 24533,
												26794, 29032, 31322, 33663,
												35752, 38170, 40429};     
}
