package org.usfirst.frc.team3042.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.util.sendable.SendableRegistry;

import org.usfirst.frc.team3042.lib.Log;
import org.usfirst.frc.team3042.robot.Robot;
import org.usfirst.frc.team3042.robot.RobotMap;
import org.usfirst.frc.team3042.robot.subsystems.Drivetrain;

/** Drivetrain Gyro Straight **************************************************
 * Command for driving straight using gyroscope feedback. */
public class Drivetrain_GyroStraight extends CommandBase {
	/** Configuration Constants ***********************************************/
	private static final Log.Level LOG_LEVEL = RobotMap.LOG_DRIVETRAIN;
	private static final double kP = RobotMap.kP_GYRO;
	private static final double CIRCUMFRENCE = RobotMap.kWheelDiameterMeters * Math.PI;
	
	/** Instance Variables ****************************************************/
	Drivetrain drivetrain = Robot.drivetrain;
	Log log = new Log(LOG_LEVEL, SendableRegistry.getName(drivetrain));

	double forwardSpeed, sideSpeed, goalAngle, goalDistance;
	double frontLeftStart, frontRightStart, backLeftStart, backRightStart;
	
	/** Drivetrain Gyro Straight **********************************************
	 * Required subsystems will cancel commands when this command is run */
	public Drivetrain_GyroStraight(double distance, double xSpeed, double ySpeed) { // distance is measured in meters and speed is measured in meters per second
		log.add("Constructor", Log.Level.TRACE);
		forwardSpeed = xSpeed;
		sideSpeed = ySpeed;
		
		// convert distance to revolutions
		goalDistance = distance / CIRCUMFRENCE;

		addRequirements(drivetrain);
	}
	
	/** initialize ************************************************************
	 * Called just before this Command runs the first time */
	public void initialize() {
		log.add("Initialize", Log.Level.TRACE);
		drivetrain.stopModules();
		goalAngle = drivetrain.getGyroAngle();
		
		// Get the current/initial position of all the drive encoders
		frontLeftStart = drivetrain.getFrontLeft().getDrivePosition();
		frontRightStart = drivetrain.getFrontRight().getDrivePosition();
		backLeftStart = drivetrain.getBackLeft().getDrivePosition();
		backRightStart = drivetrain.getBackRight().getDrivePosition();
	}

	/** execute ***************************************************************
	 * Called repeatedly when this Command is scheduled to run */
	public void execute() {
		double error = goalAngle - drivetrain.getGyroAngle();
		
		double correction = kP * error;

		// Prevent setting over 100% power to the motors
		correction = Math.min(1, correction);
		correction = Math.max(-1, correction);
		
		drivetrain.drive(forwardSpeed, sideSpeed, -1 * correction, false);
	}
	
	/** isFinished ************************************************************	
	 * Make this return true when this Command no longer needs to run execute() */
	public boolean isFinished() {
		boolean leftFrontGoalReached = Math.abs(drivetrain.getFrontLeft().getDrivePosition()) >= goalDistance;
		boolean rightFrontGoalReached = Math.abs(drivetrain.getFrontRight().getDrivePosition()) >= goalDistance;
		return leftFrontGoalReached || rightFrontGoalReached;
	}

	// Called once the command ends or is interrupted.
	public void end(boolean interrupted) {
		log.add("End", Log.Level.TRACE);
		drivetrain.stopModules();
	}
}