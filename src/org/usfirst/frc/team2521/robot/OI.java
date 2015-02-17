package org.usfirst.frc.team2521.robot;

import org.usfirst.frc.team2521.robot.commands.ConveyorBinPickup;
import org.usfirst.frc.team2521.robot.commands.MaintainConveyor;
import org.usfirst.frc.team2521.robot.commands.MoveConveyor;
import org.usfirst.frc.team2521.robot.commands.MoveToteDown;
import org.usfirst.frc.team2521.robot.commands.MoveToteUp;
//import org.usfirst.frc.team2521.robot.commands.SwitchAuto;
import org.usfirst.frc.team2521.robot.commands.ResetGyro;
import org.usfirst.frc.team2521.robot.commands.ResetAuto;
import org.usfirst.frc.team2521.robot.commands.ToggleCompressor;
import org.usfirst.frc.team2521.robot.commands.ToggleRemembering;
import org.usfirst.frc.team2521.robot.commands.Flip;
import org.usfirst.frc.team2521.robot.commands.ToggleSlowMode;
import org.usfirst.frc.team2521.robot.commands.SwitchDriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
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
	
	private Joystick translate;
	private Joystick rotate;
	private Joystick custom;
	private static OI instance;
	Preferences prefs;
	//public static boolean buttonPressed;
	private JoystickButton resetGyro;
	private JoystickButton moveConveyorUp;
	private JoystickButton moveConveyorDown;
	private JoystickButton resetRemembering;
	private JoystickButton toggleRemembering;
	private JoystickButton flip;
	private JoystickButton flipToggle;
	private JoystickButton clearLimits;
	private JoystickButton resetAuto;
	private JoystickButton toggleCompressor;
	private JoystickButton toggleSlowMode;
	private JoystickButton robotOriented;
	private JoystickButton fieldOriented;
	private JoystickButton moveToteUp;
	private JoystickButton moveToteDown;
	private JoystickButton binPickup;
	//private JoystickButton switchAutonomous;
	private int[] customButton = {6,3,4,5};
	
	
	
	 public OI() {
		 translate = new Joystick(RobotMap.TRANSLATE_PORT);
		 rotate = new Joystick(RobotMap.ROTATE_PORT);
		 custom = new Joystick(RobotMap.CUSTOM_PORT);
		 initButtons();
	 }
	 
	 public static OI getInstance() {
		 if (instance == null) {
			 instance = new OI();
		 }
		 return instance;
	 }
	
	public Joystick getTranslateStick() {
		return translate;
	}
	
	public Joystick getRotateStick() {
		return rotate;
	}
	
	
	private void initButtons() {
		resetGyro = new JoystickButton(translate, 10);
		moveConveyorUp = new JoystickButton(rotate, 3);
		moveConveyorDown = new JoystickButton(rotate, 2);
		//resetRemembering = new JoystickButton(translate, 9);
		toggleRemembering = new JoystickButton(translate, 11);
		resetAuto = new JoystickButton(translate, 9);
		toggleSlowMode = new JoystickButton(custom, 2);
		robotOriented = new JoystickButton(rotate, 10);
		fieldOriented = new JoystickButton(rotate, 11);
		//switchAutonomous = new JoystickButton(translate, 4);
		moveToteUp  = new JoystickButton(rotate, 6);
	 	moveToteDown = new JoystickButton(rotate, 7);
	 	binPickup = new JoystickButton(rotate, 8);
		tieButtons();
	}
	
	private void tieButtons() {
		resetGyro.whenPressed(new ResetGyro());
		moveConveyorUp.whileHeld(new MoveConveyor(-RobotMap.CONVEYOR_SPEED_HI));
		moveConveyorUp.whenReleased(new MaintainConveyor());
		moveConveyorDown.whenReleased(new MaintainConveyor());
		moveConveyorDown.whileHeld(new MoveConveyor(RobotMap.CONVEYOR_SPEED_LO));
		resetAuto.whenPressed(new ResetAuto());
		//resetRemembering.whenPressed(new ResetRemembering());
		toggleRemembering.whenPressed(new ToggleRemembering());
		toggleSlowMode.whenPressed(new ToggleSlowMode(true));
		toggleSlowMode.whenReleased(new ToggleSlowMode(false));
		//switchAutonomous.whileHeld(new SwitchAuto());
		//buttonPressed = translate.getRawButton(4);
		fieldOriented.whenPressed(new SwitchDriveMode(DriveMode.robotOrientedMecanum));
		robotOriented.whenPressed(new SwitchDriveMode(DriveMode.fieldOrientedMecanum));
		moveToteUp.whenPressed(new MoveToteUp());
		moveToteDown.whenPressed(new MoveToteDown());
		binPickup.whenPressed(new ConveyorBinPickup());
	}
	
	
	public Autonomous getAutoMode() {
		int autoMode = 0;
		for (int i = 0; i  < customButton.length; i++) {
				autoMode += (custom.getRawButton(customButton[i]) ? 1 : 0) << i;
		} 
		
		
		
		SmartDashboard.putNumber("autoMode", autoMode);
		switch (autoMode) {
		case 15: return Autonomous.threeTotes;
		case 14: return Autonomous.twoTotesToLandmarkRight; 
		case 12: return Autonomous.twoTotesToLandmarkLeft;
		case 11: return Autonomous.toteToLandmarkLeft;
		case 7: return Autonomous.toteToLandmarkMiddle;
		case 13: return Autonomous.toteToLandmarkRight;
		case 9: return Autonomous.toteAndBinLeft;
		case 5: return Autonomous.toteAndBinMiddle;
		case 3: return Autonomous.toteAndBinRight;
		case 8: return Autonomous.binLeft;
		case 4: return Autonomous.binMiddle;
		case 2: return Autonomous.binRight;
		default: return Autonomous.binLeft;
		
		}
	}
	
	public enum Autonomous {
		toteAndBinRight (3),
		toteAndBinMiddle (5),
		toteAndBinLeft (9),
		toteToLandmarkRight (13),
		toteToLandmarkMiddle (7),
		toteToLandmarkLeft (11),
		binRight (2),
		binMiddle (4),
		binLeft (8),
		twoTotesToLandmarkLeft (12),
		twoTotesToLandmarkRight (14),
		threeTotes (15);
		
		private final int mode;
		private Autonomous(int mode) {
			this.mode = mode;
		}
	}
	
	
	
	
}

