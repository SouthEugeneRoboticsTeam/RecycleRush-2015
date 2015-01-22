
package org.usfirst.frc.team2521.robot;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.usfirst.frc.team2521.robot.commands.Auto1;
import org.usfirst.frc.team2521.robot.commands.Auto2;
import org.usfirst.frc.team2521.robot.commands.SwitchDriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Conveyor;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Sensors;
import org.usfirst.frc.team2521.robot.subsystems.CompressorSub;
import org.usfirst.frc.team2521.robot.subsystems.Flipper;

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
	public static Sensors sensors;
	public static Drivechain drivechain;
	public static Conveyor conveyor;
	public static CompressorSub compressor;
	public static Flipper flipper;
	public static OI oi;
	SendableChooser autoChooser;
	Command autonomousCommand;

	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
    public void robotInit() {
    	

    	drivechain = new Drivechain();
    	//Auto1 auto1 = new Auto1();
    	//Auto2 auto2 = new Auto2();
    	autoChooser = new SendableChooser();
    	autoChooser.addDefault("Autonomous 1", new Auto1());
    	autoChooser.addObject("Autonomous 2", new Auto2());
		sensors = new Sensors();
		conveyor = new Conveyor();
		compressor = new CompressorSub();
		flipper = new Flipper();
		oi = new OI();
		SmartDashboard.putData("Field Oriented Drive", new SwitchDriveMode(DriveMode.fieldOrientedMecanum));
		SmartDashboard.putData("Robot Oriented Drive", new SwitchDriveMode(DriveMode.robotOrientedMecanum));
		SmartDashboard.putData("Arcade Drive", new SwitchDriveMode(DriveMode.arcadeDrive));
		SmartDashboard.putData("TankDrive", new SwitchDriveMode(DriveMode.tankDrive));
		SmartDashboard.putData("Autonomous mode", autoChooser);
        // instantiate the command used for the autonomous period
         
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    	if (OI.getInstance().getRotateStick().getRawButton(7)) {
    		autonomousCommand = new Auto1();
    	} else {
    		autonomousCommand = new Auto2();
    	}
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
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
