package org.usfirst.frc.team2521.robot.commands;

import org.usfirst.frc.team2521.robot.OI;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousGroup extends CommandGroup {
    
    public  AutonomousGroup() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	switch (OI.getInstance().getAutoMode()) {
    	case toteAndBinLeft:
    		addSequential(new PolarDrive(.7, 0, 0), 3);
    		break;
    	case toteAndBinMiddle: case toteAndBinRight:
    		addSequential(new PolarDrive(.8, 0, 0), 3);
    		break;
    	case binLeft:
    		addSequential(new PolarDrive(.6, 0, 0), 3);
    		break;
    	case binMiddle: case binRight:
    		addSequential(new PolarDrive(.7, 0, 0), 3);
    		break;
    	case toteToLandmarkMiddle:
    		addSequential(new MoveConveyor(-.5), .5);
    		addSequential(new MoveConveyor(0));
    		addSequential(new PolarDrive(.3, 180, 0), .3);
    		addSequential(new PolarDrive(.3, 90, 0), .3);
    		addSequential(new PolarDrive(1, 0, 0), 5);
    		addSequential(new MoveConveyor(.5), .5);
    		addSequential(new PolarDrive(.3, 180, 0), .5);
    		break;
    	case toteToLandmarkLeft:
    		addSequential(new PolarDrive(-.5, 270, 0), .5);
    		addSequential(new MoveConveyor(.5), .5);
    		addSequential(new PolarDrive(1, 270, 0), 4);
    		addSequential(new PolarDrive(1, 0, 0), 3);
    		addSequential(new MoveConveyor(.5), .5);
    		addSequential(new PolarDrive(.3, 180, 0), .5);
    		break;
    	case toteToLandmarkRight:
    		addSequential(new MoveConveyor(-.5), .5);
    		addSequential(new PolarDrive(.5, 270, 0), 1.5);
    		addSequential(new PolarDrive(.5, 0, 0), 1);
    		addSequential(new PolarDrive(.8, 90, 0), 6);
    		addSequential(new PolarDrive(.7, 0, 0), 1);
    		addSequential(new MoveConveyor(.5), .5);
    		addSequential(new PolarDrive(.3, 180, 0), .5);
    		break;
    	}
    	
    	
    }
}
