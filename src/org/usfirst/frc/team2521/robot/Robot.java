
package org.usfirst.frc.team2521.robot;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.usfirst.frc.team2521.robot.commands.Auto1;
import org.usfirst.frc.team2521.robot.commands.Auto2;
import org.usfirst.frc.team2521.robot.commands.AutonomousGroup;
import org.usfirst.frc.team2521.robot.commands.SwitchDriveMode;
import org.usfirst.frc.team2521.robot.commands.TeleopReset;
import org.usfirst.frc.team2521.robot.subsystems.Conveyor;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Sensors;
import org.usfirst.frc.team2521.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import java.io.File;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	public static Drivechain drivechain;
	public static Conveyor conveyor;
	//public static CompressorSub compressor;
	public static Sensors sensors;
	public static OI oi;
	SendableChooser autoChooser;
	Command autonomousCommand;
	Command teleopReset;
	public DigitalOutput bling;
	public static FileManager fileManager;// later try making a static filemanager in each class, so we can just use createLog for record auto
	//public static double conveyorSpeed = 0;
	

	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
    public void robotInit() {
    	fileManager = new FileManager();
    	RobotMap.DATE = fileManager.getFormattedDate();
    	conveyor = new Conveyor();
    	drivechain = new Drivechain();
    	autoChooser = new SendableChooser();
    	autoChooser.addDefault("Autonomous 1", new Auto1());
    	autoChooser.addObject("Autonomous 2", new Auto2());
		sensors = new Sensors();
		oi = new OI();
		SmartDashboard.putData("Field Oriented Drive", new SwitchDriveMode(DriveMode.fieldOrientedMecanum));
		SmartDashboard.putData("Robot Oriented Drive", new SwitchDriveMode(DriveMode.robotOrientedMecanum));
		SmartDashboard.putData("Arcade Drive", new SwitchDriveMode(DriveMode.arcadeDrive));
		SmartDashboard.putData("TankDrive", new SwitchDriveMode(DriveMode.tankDrive));
		SmartDashboard.putData("Autonomous mode", autoChooser);
    	teleopReset = new TeleopReset();

        // instantiate the command used for the autonomous period
		bling = new DigitalOutput(RobotMap.BLING_CHANNEL);
         
    }
    
    
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    	teleopReset.start();
    	/*if (OI.getInstance().getRotateStick().getRawButton(7)) {
    		autonomousCommand = new Auto1();
    	} else {
    		autonomousCommand = new Auto2();
    	}*/
    	autonomousCommand = new AutonomousGroup();
        // schedule the autonomous command (example)
        //if (autonomousCommand != null) autonomousCommand.start();
//    	autonomousCommand = (Command) autoChooser.getSelected();
    	autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        //if (autonomousCommand != null) autonomousCommand.cancel();
    	teleopReset.start();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        if (conveyor.getHallEffect()) {
        	//conveyor.resetPosition();
        }
        SmartDashboard.putNumber("Position", conveyor.getPosition());
        SmartDashboard.putBoolean("Hall Effect", conveyor.getHallEffect());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
