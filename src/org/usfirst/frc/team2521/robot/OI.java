package org.usfirst.frc.team2521.robot;

import org.usfirst.frc.team2521.robot.commands.CalibrateConveyor;
import org.usfirst.frc.team2521.robot.commands.ConveyorBinPickup;
import org.usfirst.frc.team2521.robot.commands.ConveyorBinStepPickup;
import org.usfirst.frc.team2521.robot.commands.ConveyorHorizontalBinPickup;
import org.usfirst.frc.team2521.robot.commands.MaintainConveyor;
import org.usfirst.frc.team2521.robot.commands.MoveConveyor;
import org.usfirst.frc.team2521.robot.commands.MoveToteDown;
import org.usfirst.frc.team2521.robot.commands.MoveToteUp;
import org.usfirst.frc.team2521.robot.commands.ResetGyro;
import org.usfirst.frc.team2521.robot.commands.ToggleSlowMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
	private static OI instance;
	
	private Joystick translate;
	private Joystick rotate;
	private Joystick custom;

	private JoystickButton resetGyro;
	private JoystickButton toggleSlowMode;
	
	private JoystickButton moveConveyorUp;
	private JoystickButton moveConveyorDown;
	private JoystickButton moveToteUp;
	private JoystickButton moveToteDown;
	private JoystickButton binPickup;
	private JoystickButton binStepPickup;
	private JoystickButton maintainConveyor;
	private JoystickButton binHorizontalPickup;
	private JoystickButton conveyorReset;
	
	private int[] customButton = {6,3,4,5};
	
	public static OI getInstance() {
		 if (instance == null) {
			 instance = new OI();
		 }
		 return instance;
	}
	
	public OI() {
		 translate = new Joystick(RobotMap.TRANSLATE_PORT);
		 rotate = new Joystick(RobotMap.ROTATE_PORT);
		 custom = new Joystick(RobotMap.CUSTOM_PORT);
		 initButtons();
	}
	 

	
	public Joystick getTranslateStick() {
		return translate;
	}
	
	public Joystick getRotateStick() {
		return rotate;
	}
	
	
	private void initButtons() {
		resetGyro = new JoystickButton(translate, 10);
		toggleSlowMode = new JoystickButton(custom, 2);
		
		moveConveyorUp = new JoystickButton(rotate, 3);
		moveConveyorDown = new JoystickButton(rotate, 2);
		moveToteUp  = new JoystickButton(rotate, 6);
	 	moveToteDown = new JoystickButton(rotate, 7);
	 	binPickup = new JoystickButton(rotate, 8);
	 	binStepPickup = new JoystickButton(rotate, 10);
	 	binHorizontalPickup = new JoystickButton(rotate, 9);
	 	maintainConveyor = new JoystickButton(rotate, 5);
	 	conveyorReset = new JoystickButton(translate, 9);
		tieButtons();
	}
	
	private void tieButtons() {
		resetGyro.whenPressed(new ResetGyro());
		toggleSlowMode.whenPressed(new ToggleSlowMode(true));
		toggleSlowMode.whenReleased(new ToggleSlowMode(false));
		
		moveConveyorUp.whileHeld(new MoveConveyor(-RobotMap.CONVEYOR_SPEED_HI));
		moveConveyorUp.whenReleased(new MoveConveyor(0));
		moveConveyorDown.whileHeld(new MoveConveyor(RobotMap.CONVEYOR_SPEED_LO));
		moveConveyorDown.whenReleased(new MoveConveyor(0));
		moveToteUp.whenPressed(new MoveToteUp());
		moveToteDown.whenPressed(new MoveToteDown());
		binPickup.whenPressed(new ConveyorBinPickup());
		binStepPickup.whenPressed(new ConveyorBinStepPickup());
		binHorizontalPickup.whenPressed(new ConveyorHorizontalBinPickup());
		maintainConveyor.whenPressed(new MaintainConveyor());
		conveyorReset.whenPressed(new CalibrateConveyor());
	}
	
	
	public Autonomous getAutoMode() {
		int autoMode = 0;
		for (int i = 0; i  < customButton.length; i++) {
				autoMode += (custom.getRawButton(customButton[i]) ? 1 : 0) << i;
		} 
		
		
		
		SmartDashboard.putNumber("autoMode", autoMode);
		switch (autoMode) {
		case 15: return Autonomous.backFromLandfill;
		//case 14: return Autonomous.twoTotesToLandmarkRight; 
		case 12: return Autonomous.twoTotesToLandmarkLeft;
		case 11: return Autonomous.toteToLandmarkLeft;
		case 7: return Autonomous.threeTotes;
		case 13: return Autonomous.toteToLandmarkRight;
		case 9: return Autonomous.toteAndBinLeft;
		case 5: return Autonomous.toteAndBinMiddle;
		case 3: return Autonomous.toteAndBinRight;
		case 8: return Autonomous.binLeft;
		case 4: return Autonomous.binMiddle;
		case 2: return Autonomous.binRight;
		case 1: return Autonomous.twoLandfill;
		case 0: return Autonomous.nothing;
		case 14: return Autonomous.canTheftAuto;
		default: return Autonomous.binLeft;
		
		}
	}
	
	public enum Autonomous {
		toteAndBinRight,
		toteAndBinMiddle,
		toteAndBinLeft,
		toteToLandmarkRight,
		threeTotes,
		toteToLandmarkLeft,
		binRight,
		binMiddle,
		binLeft,
		twoTotesToLandmarkLeft,
		twoTotesToLandmarkRight,
		backFromLandfill,
		twoLandfill,
		nothing,
		canTheftAuto;
	}
	
	
	
	
}

