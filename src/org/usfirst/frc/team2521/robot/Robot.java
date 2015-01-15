
package org.usfirst.frc.team2521.robot;

import org.usfirst.frc.team2521.robot.commands.Autonomous;
import org.usfirst.frc.team2521.robot.commands.SwitchDriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Conveyor;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;
import org.usfirst.frc.team2521.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	public static OI oi;

     Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
    public void robotInit() {
		sensors = new Sensors();
		drivechain = new Drivechain();
		conveyor = new Conveyor();
		oi = new OI();
		SmartDashboard.putData("Autonomous", new Autonomous());
		SmartDashboard.putData("Field Oriented Drive", new SwitchDriveMode(DriveMode.fieldOrientedMecanum));
		SmartDashboard.putData("Robot Oriented Drive", new SwitchDriveMode(DriveMode.robotOrientedMecanum));
		SmartDashboard.putData("Arcade Drive", new SwitchDriveMode(DriveMode.arcadeDrive));
		SmartDashboard.putData("TankDrive", new SwitchDriveMode(DriveMode.tankDrive));
        // instantiate the command used for the autonomous period
         
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        //if (autonomousCommand != null) autonomousCommand.start();
    	autonomousCommand = new Autonomous();
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
