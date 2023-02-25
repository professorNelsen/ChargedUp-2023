// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.usfirst.frc.team3042.robot.commands;

import org.usfirst.frc.team3042.robot.Robot;
import org.usfirst.frc.team3042.robot.RobotMap;
import org.usfirst.frc.team3042.robot.subsystems.Arm;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Arm_SetPosition extends CommandBase {
  Arm arm = Robot.arm;

  public double rotationPositionGoal;
  public double extensionPositionGoal;

  /** Creates a new Arm_SetPosition command. */
  public Arm_SetPosition(double rotationGoal, double extensionPercent) { // rotationGoal is measured in encoder counts
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(arm);

    this.rotationPositionGoal = rotationGoal;
    this.extensionPositionGoal = extensionPercent * RobotMap.maxArmLength; // convert from a percent to actual encoder counts
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double rotationError = rotationPositionGoal - arm.getRotationMotorPosition();
    double extensionError = extensionPositionGoal - arm.getExtendMotorPosition();

    boolean extensionGoalReached = (Math.abs(extensionError) <= RobotMap.extensionThreshold);
    boolean inIntakePosition = Math.abs(arm.getRotationMotorPosition() - RobotMap.kIntakeArmPosition) <= RobotMap.rotationThreshold;
    boolean rotationGoalReached = (Math.abs(rotationError) <= RobotMap.rotationThreshold); //added for switching rotation before extension
  
    // When going to the intake position, we will wait for the extension to move first before rotating the arm  (changed to driverpositon which retracts before rotation)
    if (rotationPositionGoal != RobotMap.kArmDrivePosition || (rotationPositionGoal == RobotMap.kArmDrivePosition && extensionGoalReached)) {
    //added in statements for other positions, just reversing the command to rotation before extension  (guessing here) 
    if (extensionPositionGoal != RobotMap.kIntakeArmPosition || (extensionPositionGoal == RobotMap.kIntakeArmPosition && rotationGoalReached)) {
    if (extensionPositionGoal != RobotMap.kScoringArmPosition1 || (extensionPositionGoal == RobotMap.kScoringArmPosition1 && rotationGoalReached)) {
    if (extensionPositionGoal != RobotMap.kScoringArmPosition2 || (extensionPositionGoal == RobotMap.kScoringArmPosition2 && rotationGoalReached)) {
    if (extensionPositionGoal != RobotMap.kShelfIntakeArmPosition || (extensionPositionGoal == RobotMap.kShelfIntakeArmPosition && rotationGoalReached)) {
     
     // THIS BLOCK OF CODE BELOW ROTATES THE ARM SHOULDER //
      double minimalVoltage = RobotMap.levelVoltage * Math.sin(arm.getArmAngle()); // TODO: Also scale this minimalVoltage by how far the arm is extended somehow?
      arm.setVoltageRotationMotor(minimalVoltage + (rotationError * RobotMap.rotation_kP)); 

    }
  
    // We are only going to move the extension if the arm is NOT in the intake position -- NOT DONE?????
    if (!inIntakePosition) {

      // THIS BLOCK OF CODE BELOW MOVES THE EXTENSION //
      if (!extensionGoalReached) {
        arm.setPowertoExtend(Math.copySign(0.2, extensionError)); // TODO: Increase percent power if you want to make the extension move faster
        // If it ends up oscillating because it can't reach its goal position, use (extensionError * kP) instead of (Math.copySign(0.2, extensionError))
      } else {
        arm.stopExtendMotor();
      }

    }  //error here? cannot see any changes from past????
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the motors if the command is interrupted
    arm.stopExtendMotor();
    arm.stopRotationMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false; // We never want this command to end
  }
}