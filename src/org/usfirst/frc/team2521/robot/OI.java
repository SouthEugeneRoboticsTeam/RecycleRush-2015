package org.usfirst.frc.team2521.robot;

import org.usfirst.frc.team2521.robot.commands.MoveConveyor;
//import org.usfirst.frc.team2521.robot.commands.SwitchAuto;
import org.usfirst.frc.team2521.robot.commands.ResetGyro;
import org.usfirst.frc.team2521.robot.commands.ResetRemembering;
import org.usfirst.frc.team2521.robot.commands.ToggleRemembering;
import org.usfirst.frc.team2521.robot.commands.Flip;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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
	private static OI instance;
	//public static boolean buttonPressed;
	private JoystickButton resetGyro;
	private JoystickButton moveConveyorUp;
	private JoystickButton moveConveyorDown;
	private JoystickButton resetRemembering;
	private JoystickButton toggleRemembering;
	private JoystickButton flip;
	//private JoystickButton switchAutonomous;
	
	 public OI() {
		 translate = new Joystick(RobotMap.TRANSLATE_PORT);
			rotate = new Joystick(RobotMap.ROTATE_PORT);
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
		resetRemembering = new JoystickButton(translate, 9);
		toggleRemembering = new JoystickButton(translate, 11);
		flip = new JoystickButton(translate, 1);
		//switchAutonomous = new JoystickButton(translate, 4);
		tieButtons();
	}
	
	private void tieButtons() {
		resetGyro.whenPressed(new ResetGyro());
		moveConveyorUp.whileHeld(new MoveConveyor(RobotMap.CONVEYOR_SPEED));
		moveConveyorDown.whileHeld(new MoveConveyor(-RobotMap.CONVEYOR_SPEED));
		resetRemembering.whenPressed(new ResetRemembering());
		toggleRemembering.whenPressed(new ToggleRemembering());
		flip.whenPressed(new Flip());
		//switchAutonomous.whileHeld(new SwitchAuto());
		//buttonPressed = translate.getRawButton(4);
		
	}
	
}

