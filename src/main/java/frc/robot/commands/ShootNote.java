// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Onboarder;
import frc.robot.subsystems.Shooter;
import frc.utils.Alarm;

public class ShootNote extends Command {  
  private boolean isFinished;
  private Alarm timer = new Alarm(1);

  private Shooter shooter;
  private Onboarder onboarder;

  /** Creates a new ShootNote. */
  public ShootNote(Shooter shooter, Onboarder onboarder) {
    this.shooter = shooter;
    this.onboarder = onboarder;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter, onboarder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isFinished = false;
    timer.initAlarm();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setShooter(Constants.ShooterConstants.kShooterPowerValue);
    onboarder.setOnboarder(1);

    if (timer.hasTriggered()) {
      isFinished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setShooter(0);
    onboarder.setOnboarder(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
