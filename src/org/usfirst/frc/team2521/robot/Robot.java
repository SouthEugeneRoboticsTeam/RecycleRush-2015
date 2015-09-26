
package org.usfirst.frc.team2521.robot;

import org.usfirst.frc.team2521.robot.commands.AutoModeSelector;
import org.usfirst.frc.team2521.robot.commands.SwitchDriveMode;
import org.usfirst.frc.team2521.robot.commands.TeleopReset;
import org.usfirst.frc.team2521.robot.subsystems.Bling;
import org.usfirst.frc.team2521.robot.subsystems.Conveyor;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain.DriveMode;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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
	public static Bling bling;
	public static OI oi;

	SendableChooser autoChooser;
	Command autonomousCommand;
	Command teleopReset;


    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
    public void robotInit() {
    	conveyor = new Conveyor();
    	drivechain = new Drivechain();
    	bling = new Bling();
		oi = new OI();
    	teleopReset = new TeleopReset();

		SmartDashboard.putData("Field Oriented Drive", new SwitchDriveMode(DriveMode.fieldOrientedMecanum));
		SmartDashboard.putData("Robot Oriented Drive", new SwitchDriveMode(DriveMode.robotOrientedMecanum));
		SmartDashboard.putData("Arcade Drive", new SwitchDriveMode(DriveMode.arcadeDrive));
		SmartDashboard.putData("TankDrive", new SwitchDriveMode(DriveMode.tankDrive));
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    	teleopReset.start();
    	autonomousCommand = new AutoModeSelector();

    	autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
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
        if (conveyor.getReadSwitch()) {
        	conveyor.resetPosition();
        }
        SmartDashboard.putNumber("Position", conveyor.getPosition());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
