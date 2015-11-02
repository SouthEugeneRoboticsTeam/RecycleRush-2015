package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.OI;
import org.usfirst.frc.team2521.robot.subsystems.Drivechain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoModeSelector extends CommandGroup{
    
    public  AutoModeSelector() {
    	switch (OI.getInstance().getAutoMode()) {
    		
        	case binLeft:
        		addSequential(new PolarDrive(.5, 180, .075), 2.25);
        		break;
        		
        	case binMiddle: case binRight:
        		addSequential(new PolarDrive(.4, 180, .05), 3.2);
        		break;
        		
        	case toteAndBinLeft:
        		addSequential(new MoveConveyor(-.5), .5);
        		addSequential(new MoveConveyor(0),.01);
        		addSequential(new PolarDrive(.5, 180, .05), 2);
        		break;
        		
        	case toteAndBinMiddle: case toteAndBinRight:
        		addSequential(new MoveConveyor(-.5), .5);
        		addSequential(new MoveConveyor(0),.01);
        		addSequential(new PolarDrive(.45, 180, .05), 2.85);
        		break;
        		
        	case navXAutonomous:
        		try {
        			DriverStation.reportError("Yay! Autonomous code started successfully!", false);
                    while ( !Drivechain.collisionDetected ) { // Need to implement ( !Drivechain.collisionDetected || Drivechain.ahrs.getAltitude() < x value )
                    	addSequential(new NavXAuto(0, .4, 0));
                    	DriverStation.reportError("Moving forward.", false);
                    } 
                    if ( Drivechain.collisionDetected ) {
                    	DriverStation.reportError("Collison was detected.", false);
                    	addParallel(new MoveToteUp(),.5);
                    	addSequential(new NavXAuto(0, .1, 0), 1);
                    	DriverStation.reportError("Picking up tote.", false);
                    	addSequential(new NavXAuto(0, -.1, 0), 0.5);
                    	addSequential(new NavXAuto(0, .1, 0), 1);
                    	addParallel(new NavXAuto(0, 0, 179.9f), 1);
                    	DriverStation.reportError("Rotating to the right 180 degrees.", false);
                    	Drivechain.collisionDetected = false;
                    }
                } catch (RuntimeException ex ) {
                    DriverStation.reportError("Error " + ex.getMessage(), true);
                }
        		break;
        		
        	case backFromLandfill:
        		addSequential(new PolarDrive(.5, 180, 0), 1.5);
        		break;
        		
        	case threeTotes: 
        		addParallel(new MoveToteUp(),.5);
        		addSequential(new PolarDrive(.5, 0, -.75), 1);
        		addSequential(new PolarDrive(.8, 180, .05), 1.5);
        		addSequential(new PolarDrive(.5, 0, .75), 1);
        		addSequential(new PolarDrive(.2, 0, 0), .5);
        		addSequential(new MoveToteUp(), .5);
        		addSequential(new PolarDrive(.5, 0, -.75), 1);
        		addSequential(new PolarDrive(.8, 180, .05), 1.5);
        		addSequential(new PolarDrive(.5, 0, .75), 1);
        		addSequential(new PolarDrive(.2, 0, 0), .5);
        		addSequential(new MoveToteUp(), .5);
        		addSequential(new PolarDrive(.8, 180, .05), 2);
        		addSequential(new MoveToteDown(), .5);
        		addSequential(new PolarDrive(.8, 180, .05), .2);
        		break;
        		
    			default:
    				DriverStation.reportError("Error: no auto case was selected", false);
    				break;
        	}
    	
    	addSequential(new PolarDrive(0, 0, 0));
    }
}
