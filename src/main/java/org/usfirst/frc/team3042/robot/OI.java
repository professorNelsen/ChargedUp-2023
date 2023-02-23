package org.usfirst.frc.team3042.robot;

import org.usfirst.frc.team3042.lib.Log;
import org.usfirst.frc.team3042.robot.commands.Arm_SetPosition;
import org.usfirst.frc.team3042.robot.commands.Drivetrain_XStance;

import edu.wpi.first.wpilibj.GenericHID;
// import edu.wpi.first.wpilibj.Joystick; // flight joystick
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** OI ************************************************************************
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot. */
public class OI {
   
    /** Declare Instance Variables ****************************************************/
	Log log = new Log(RobotMap.LOG_OI, "OI");
    // public static final Joystick joyLeft = new Joystick(RobotMap.USB_JOYSTICK_LEFT); // Instantiate a flight joystick at the specified USB port 
    // public static final Joystick joyRight = new Joystick(RobotMap.USB_JOYSTICK_RIGHT); // Instantiate a flight joystick at the specified USB port 
	public static final GenericHID driverController = new GenericHID(RobotMap.DRIVER_XBOX_USB_PORT);
	public static final GenericHID gunnerController = new GenericHID(RobotMap.GUNNER_XBOX_USB_PORT);

	public boolean getRightTrigger(GenericHID gunnerController){
		return gunnerController.getRawAxis(3) >= 0.95 ;
	}

	/** OI ********************************************************************
	 * Assign commands to the buttons and triggers */
	public OI() {
		log.add("OI Constructor", Log.Level.TRACE);

        // BIND COMMANDS/ACTIONS TO BUTTONS ON THE CONTROLLERS BELOW //

		/* Drivetrain actions */
		new Trigger(() -> driverController.getRawButton(RobotMap.RIGHT_BUMPER)).onTrue(new InstantCommand(Robot.drivetrain::zeroGyro, Robot.drivetrain)); // Zero the gyro, this is helpful at the start of a match for field-oriented driving
		new Trigger(() -> driverController.getRawButton(RobotMap.X_BUTTON)).onTrue(new Drivetrain_XStance()); // Defensive X-stance command

		/* Gripper Actions */
        new Trigger(() -> gunnerController.getRawButton(RobotMap.LEFT_BUMPER)).onTrue(new InstantCommand(() -> Robot.gripper.toggle()));

		/* Arm Actions */
		new Trigger(() -> gunnerController.getRawButton(RobotMap.A_BUTTON)).onTrue(new Arm_SetPosition(RobotMap.kIntakeArmPosition, RobotMap.kIntakeExtendPosition));
		new Trigger(() -> gunnerController.getRawButton(RobotMap.X_BUTTON)).onTrue(new Arm_SetPosition(RobotMap.kScoringArmPosition1,RobotMap.kScoringExtendPosition1));
		new Trigger(() -> gunnerController.getRawButton(RobotMap.B_BUTTON)).onTrue(new Arm_SetPosition(RobotMap.kArmDrivePosition,RobotMap.kExtendDrivePoistion));
		new Trigger(() -> gunnerController.getRawButton(RobotMap.Y_BUTTON)).onTrue(new Arm_SetPosition(RobotMap.kScoringArmPosition2,RobotMap.kScoringExtendPosition2));
		new Trigger(() -> gunnerController.getRawButton(RobotMap.RIGHT_BUMPER)).onTrue(new Arm_SetPosition(RobotMap.kShelfIntakeArmPosition,RobotMap.kShelfIntakeExtendPosition));

	}

    /** Access to the driving axes values *****************************
	 * A negative can be added to make pushing forward positive/negative. */
	public double getXSpeed() {
		double joystickValue = driverController.getRawAxis(1);
		// double joystickValue = driverController.getRightY();
		if (Math.abs(joystickValue) < 0.05) { // This is our deadband
			return 0.0;
		}
		else {
			return joystickValue * RobotMap.kPhysicalMaxSpeedMetersPerSecond * -1; // Multiply by -1 reverses the direction
		}	
	}
	public double getYSpeed() {
		// double joystickValue = joyRight.getX();
		double joystickValue = driverController.getRawAxis(0);
		if (Math.abs(joystickValue) < 0.05) { // This is our deadband
			return 0.0;
		}
		else {
			return joystickValue * RobotMap.kPhysicalMaxSpeedMetersPerSecond * -1; // Multiply by -1 reverses the direction, 0.5 to reduce speed
		}	
	}
	public double getZSpeed() {
		// double joystickValue = joyLeft.getX();
		double joystickValue = driverController.getRawAxis(4);
		if (Math.abs(joystickValue) < 0.05) { // This is our deadband
			return 0.0;
		}
		else {
			return joystickValue * RobotMap.kPhysicalMaxTurningSpeedRadiansPerSecond * -1; // Multiply by -1 reverses the direction
		}	
	}
}