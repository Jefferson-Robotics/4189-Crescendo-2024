// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.AmpSystem;

public class DriveAmpSystem extends Command {
  private AmpSystem ampSystem;
  private CommandXboxController operatorController;
  /** Creates a new DriveActuate. */
  public DriveAmpSystem(CommandXboxController operatorController, AmpSystem ampSystem) {
    this.ampSystem = ampSystem;
    this.operatorController = operatorController;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(ampSystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ampSystem.setActuate(operatorController.getRightY());
    if (operatorController.getLeftTriggerAxis() != 0) {
      ampSystem.setRoller(operatorController.getLeftTriggerAxis());
    } else if (operatorController.getRightTriggerAxis() != 0) {
      ampSystem.setRoller(-operatorController.getRightTriggerAxis());
    } else {
      ampSystem.setRoller(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ampSystem.setActuate(0);
    ampSystem.setRoller(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
