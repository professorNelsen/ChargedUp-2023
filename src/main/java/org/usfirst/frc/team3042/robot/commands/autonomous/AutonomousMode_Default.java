package org.usfirst.frc.team3042.robot.commands.autonomous;

import org.usfirst.frc.team3042.robot.commands.Drivetrain_GyroStraight;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/** Autonomous Mode (Default) ******************************************************
 * This is a basic autonomous routine */
public class AutonomousMode_Default extends SequentialCommandGroup {

  public AutonomousMode_Default() {
    addCommands(new Drivetrain_GyroStraight(0.4, 0.9, 0));
    addCommands(new Drivetrain_GyroStraight(0.15, 0.3, 0));
  }
}