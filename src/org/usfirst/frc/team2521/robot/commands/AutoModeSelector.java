package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.OI;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoModeSelector extends CommandGroup {
    
    public  AutoModeSelector() {
    	switch (OI.getInstance().getAutoMode()) {
    	case binLeft:
    		addSequential(new MoveConveyor(-.5), 1);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.5, 180, .075), 2.25);
    		break;
    	case binMiddle: case binRight:
    		addSequential(new MoveConveyor(-.5), 1);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.4, 180, .05), 3.2);
    		break;
    	case toteAndBinLeft:
    		addSequential(new MoveConveyor(-.5), .75);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.5, 180, .05), 2.2);
    		break;
    	case toteAndBinMiddle: case toteAndBinRight:
    		addSequential(new MoveConveyor(-.5), .75);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.45, 180, .05), 2.85);
    		break;
    	case nothing:
    		addSequential(new MoveConveyor(-.5), .75);
    		addSequential(new MoveConveyor(0),.01);
    		break;
    	case backFromLandfill:
    		addSequential(new MoveConveyor(-.5), .75);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.5, 180, 0), 1.5);
    		break;
    		
    	case twoLandfill:
    		addSequential(new MoveConveyor(-.5), 1);
    		addSequential(new MoveToteUp(), 2);
    		addSequential(new PolarDrive(.25, 0, 0), 2);
    		addSequential(new PolarDrive(0, 0, 0), .01);
    		
    		addSequential(new MoveConveyor(-.4), 1.75);
    		addSequential(new MoveConveyor(0), .2);
    		addSequential(new MoveToteUp(), 1);
    		addSequential(new PolarDrive(.2, 0, 0), 2.5);
    		addSequential(new PolarDrive(0, 0, 0), .5);
    		addSequential(new MoveConveyor(-.4), .75);
    		addSequential(new MoveConveyor(0), .01);
    		addSequential(new PolarDrive(.5, 180, 0), 1.8);
//    		addSequential(new PolarDrive(0, 0, .4), 1.25);
    		break;
    		
    	case grabBin:
    		addSequential(new ToggleArm());
    		addSequential(new MoveConveyor(-.5), .75);
    		addSequential(new MoveConveyor(0),.01);
    		addSequential(new PolarDrive(.5, 180, 0), 1.5);
    		addSequential(new ToggleArm());
    		break;
    		
    	default:
			addSequential(new MoveConveyor(-.5), .75);
			break;
//    	case threeTotes: 
//    		addParallel(new MoveToteUp(),.5);
//    		addSequential(new PolarDrive(.5, 0, -.75), 1);
//    		addSequential(new PolarDrive(.8, 180, .05), 1.5);
//    		addSequential(new PolarDrive(.5, 0, .75), 1);
//    		addSequential(new PolarDrive(.2, 0, 0), .5);
//    		addSequential(new MoveToteUp(), .5);
//    		addSequential(new PolarDrive(.5, 0, -.75), 1);
//    		addSequential(new PolarDrive(.8, 180, .05), 1.5);
//    		addSequential(new PolarDrive(.5, 0, .75), 1);
//    		addSequential(new PolarDrive(.2, 0, 0), .5);
//    		addSequential(new MoveToteUp(), .5);
//    		addSequential(new PolarDrive(.8, 1d80, .05), 2);
//    		addSequential(new MoveToteDown(), .5);
//    		addSequential(new PolarDrive(.8, 180, .05), .2);
//    		break;
//    	case toteToLandmarkMiddle:
//    		addSequential(new MoveConveyor(-.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(.3, 180, 0), .3);
//    		addSequential(new PolarDrive(.3, 90, 0), .3);
//    		addSequential(new PolarDrive(1, 0, 0), 5);
//    		addSequential(new MoveConveyor(.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(.3, 180, 0), .5);
//    		break;
//    	case toteToLandmarkLeft:
//    		addSequential(new PolarDrive(-.5, 270, 0), .5);
//    		addSequential(new MoveConveyor(-.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(1, 270, 0), 4);
//    		addSequential(new PolarDrive(1, 0, 0), 3);
//    		addSequential(new MoveConveyor(.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(.3, 180, 0), .5);
//    		break;
//    	case toteToLandmarkRight:
//    		addSequential(new MoveConveyor(-.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(.5, 270, 0), 1.5);
//    		addSequential(new PolarDrive(.5, 0, 0), 1);
//    		addSequential(new PolarDrive(.8, 90, 0), 6);
//    		addSequential(new PolarDrive(.7, 0, 0), 1);
//    		addSequential(new MoveConveyor(.5), .5);
//    		addSequential(new MoveConveyor(0));
//    		addSequential(new PolarDrive(.3, 180, 0), .5);
//    		break;
    	}
    	addSequential(new MoveConveyor(0), .01);
    	addSequential(new PolarDrive(0, 0, 0));
    	
    }
}
