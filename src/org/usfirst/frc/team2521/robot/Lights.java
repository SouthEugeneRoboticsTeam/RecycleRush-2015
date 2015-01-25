package org.usfirst.frc.team2521.robot;

import edu.wpi.first.wpilibj.DigitalOutput;

public class Lights {
	DigitalOutput allLights;
	DigitalOutput light1;
	DigitalOutput light2;
	DigitalOutput light3;
	DigitalOutput light4;
	DigitalOutput light5;
	DigitalOutput light6;
	DigitalOutput light7;

	public void Lights(){
		DigitalOutput allLights = new DigitalOutput(RobotMap.LIGHT_MAIN);
		DigitalOutput light1 = new DigitalOutput(RobotMap.LIGHT_1);
		DigitalOutput light2 = new DigitalOutput(RobotMap.LIGHT_2);
		DigitalOutput light3 = new DigitalOutput(RobotMap.LIGHT_3);
		DigitalOutput light4 = new DigitalOutput(RobotMap.LIGHT_4);
		DigitalOutput light5 = new DigitalOutput(RobotMap.LIGHT_5);
		DigitalOutput light6 = new DigitalOutput(RobotMap.LIGHT_6);
		DigitalOutput light7 = new DigitalOutput(RobotMap.LIGHT_7);
	}
	
	public void turnAllOn(){
		allLights.set(false);
		light1.set(true);
		light2.set(true);
		light3.set(true);
		light4.set(true);
		light5.set(true);
		light6.set(true);
		light7.set(true);
	}
	
	public void turnAllOff(){
		allLights.set(false);
		light1.set(false);
		light2.set(false);
		light3.set(false);
		light4.set(false);
		light5.set(false);
		light6.set(false);
		light7.set(false);
	}
	
	/*public void turnOn(int num){
		switch (num) {
		case 1:
			light1
		}*/
	}
//}
