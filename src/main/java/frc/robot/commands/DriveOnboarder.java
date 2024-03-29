// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Onboarder;

public class DriveOnboarder extends Command {
  /** Creates a new DriveOnboarder. */
  private Onboarder onboarder;
  private CommandXboxController operatorController;

  public DriveOnboarder(Onboarder onboarder, CommandXboxController operatorController) {
    this.onboarder = onboarder;
    this.operatorController = operatorController;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(onboarder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    onboarder.setOnboarder(operatorController.getLeftY());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    onboarder.setOnboarder(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
