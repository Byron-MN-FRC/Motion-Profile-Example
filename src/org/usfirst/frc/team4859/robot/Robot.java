/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4859.robot;


import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	TalonSRX talon = new TalonSRX(1);
	TalonSRX follower = new TalonSRX(2);
	Joystick joy = new Joystick(0);
	
	boolean lastButton;
	
	MotionProfileExample _example = new MotionProfileExample(talon);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		talon.setSensorPhase(false); /* keep sensor and motor in phase */

		talon.config_kF(0, 0.12, 10);
		talon.config_kP(0, 0.24, 10);
		talon.config_kI(0, 0.00005, 10);
		talon.config_kD(0, 0.0, 10);

		/* Our profile uses 10ms timing */
		talon.configMotionProfileTrajectoryPeriod(10, 10); 
		/*
		 * status 10 provides the trajectory target for motion profile AND
		 * motion magic
		 */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
	}

	
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		double leftYjoystick = -joy.getY();
//		System.out.print("Joystick: " + leftYjoystick);
//		System.out.println(" Sensor: " + _talon.getSelectedSensorPosition(0));
//		
//		System.out.println(_talon.getMotorOutputPercent());
//		System.out.println(_follower.getMotorOutputPercent());
		_example.control();
		
		if (joy.getRawButton(5)) {
			SetValueMotionProfile setOutput = _example.getSetValue();

			talon.set(ControlMode.MotionProfile, setOutput.value);
			follower.set(ControlMode.Follower, 1);
			if ((joy.getRawButton(5) == true) && (lastButton == false)) {
				_example.startMotionProfile();
			}
			
			
		}
		
		lastButton = joy.getRawButton(5);
	}
	
	public void disabledPeriodic() {
		talon.set(ControlMode.PercentOutput, 0);
		/* clear our buffer and put everything into a known state */
		_example.reset();
	}

	/**
	 * This function is called periodically during test mode.
	 */

}
