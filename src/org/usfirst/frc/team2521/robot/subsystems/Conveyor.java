package org.usfirst.frc.team2521.robot.subsystems;

import org.usfirst.frc.team2521.robot.RobotMap;
import org.usfirst.frc.team2521.robot.commands.MaintainConveyor;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class Conveyor extends Subsystem {
	private CANTalon master;
	private CANTalon slave;
	private DigitalInput magReadSwitch;
//	private InterruptHandlerFunction<Conveyor> magReadHandler;
	
	public Conveyor() {
		master = new CANTalon(RobotMap.CONVEYOR_MASTER);
		slave = new CANTalon(RobotMap.CONVEYOR_SLAVE);
		
		master.changeControlMode(ControlMode.PercentVbus);
		slave.changeControlMode(ControlMode.Follower);
		slave.set(RobotMap.CONVEYOR_MASTER);
		master.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		master.setPID(3, 0, 1);
		
		magReadSwitch = new DigitalInput(RobotMap.MAG_READ_CHANNEL);
//		magReadSwitch.setUpSourceEdge(false, true);
//		magReadSwitch.requestInterrupts(magReadHandler);
	}
	
	
	public void moveConveyor(double speed) {
		master.changeControlMode(ControlMode.PercentVbus);
		master.enableControl();
		master.set(speed);
	}
	
	public boolean getReadSwitch() {
		return !magReadSwitch.get(); // Switch is pulled to ground when closed
	}
	
	public void resetPosition() {
		master.setPosition(0);
	}
	
	public void setPID(double p, double i, double d) {
		master.setPID(p, i, d);
	}
	
	public int getPosition() {
		return (int) master.getPosition();
	}
	
	public void setPosition(int position) {
		master.changeControlMode(ControlMode.Position);
		master.set(position);
		master.enableControl();
	}
	
	public void moveUpOneHook() {
		int currentRelativePosition = getPosition() % RobotMap.CODES_PER_CYCLE;
		setPosition(getPosition() + (getClosestPositions(RobotMap.HOOK_POSITIONS, currentRelativePosition, Direction.down) + currentRelativePosition));
	}


	public void moveDownOneHook() {
		int currentRelativePosition = getPosition() % RobotMap.CODES_PER_CYCLE;
		setPosition(getPosition() + (getClosestPositions(RobotMap.HOOK_POSITIONS, currentRelativePosition, Direction.up) + currentRelativePosition));
	}
	
	public void binPickup() {
		int currentRelativePosition = getPosition() % RobotMap.CODES_PER_CYCLE;
		setPosition(getPosition() + (getClosestPositions(RobotMap.BIN_PICKUPS, currentRelativePosition, Direction.none) + currentRelativePosition));
	}
	
	public void binStepPickup() {
		int currentRelativePosition = getPosition() % RobotMap.CODES_PER_CYCLE;
		setPosition(getPosition() + (getClosestPositions(RobotMap.BIN_STEP_PICKUPS, currentRelativePosition, Direction.none) + currentRelativePosition));
	}
	
	private int getClosestPositions(int[] positions, int target, Direction direction) {

	    int low = 0;
	    int high = positions.length - 1;

	    while (low < high) {
	        int mid = (low + high) / 2;
	        int d1 = Math.abs(positions[mid] - target);
	        int d2 = Math.abs(positions[mid + 1] - target);
	        if (d2 <= d1) {
	            low = mid + 1;
	        } else {
	            high = mid;
	        }
	    }
	    
	    return positions[high + direction.value];
	}

	public enum Direction {
		up (-1),
		down (1),
		none (0);
		
		private final int value;
		private Direction(int value) {
			this.value = value;
		}
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new MaintainConveyor(getPosition()));
    }
}

