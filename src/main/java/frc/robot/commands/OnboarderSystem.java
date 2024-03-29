// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Onboarder;

public class OnboarderSystem extends Command {
  /** Creates a new OnboarderSystem. */
  private Onboarder onboarder;
  private CommandXboxController operatorController;
  private boolean joystickControl;
  private boolean intake;

  public OnboarderSystem(Onboarder onboarder, CommandXboxController operatorController, boolean joystickControl, boolean intake) {
    this.onboarder = onboarder;
    this.operatorController = operatorController;
    this.joystickControl = joystickControl;
    this.intake = intake;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(onboarder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (joystickControl) {
      // For joystick override
      if (intake) {
        onboarder.setOnboarder(1);
      } else {
        onboarder.setOnboarder(-1);
      }
    } else {
      // For Auto-Intake & Operator override
      if (Math.abs(operatorController.getLeftY()) > .1) {
        onboarder.setOnboarder(-operatorController.getLeftY());
      } else if(onboarder.getShooterSensor()){
        onboarder.setOnboarder(0);
      } else if(onboarder.getBumperSensor()) {
        onboarder.setOnboarder(0.75);
      }else {
        onboarder.setOnboarder(0);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
