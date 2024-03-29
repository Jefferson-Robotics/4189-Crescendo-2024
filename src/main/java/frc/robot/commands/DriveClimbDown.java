// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climb;

public class DriveClimbDown extends Command {
  private Climb climb;

  /** Creates a new DriveClimbDown. */
  public DriveClimbDown(Climb climb) {
    this.climb = climb;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climb);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!climb.getExtendedSensor()) {
      climb.setClimb(1);
    } else {
      climb.setClimb(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climb.setClimb(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
