// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.AmpSystem;
import frc.robot.subsystems.Onboarder;
import frc.robot.subsystems.Shooter;

public class AmpOnboard extends Command {
  /** Creates a new AmpOnboard. */
  private boolean isFinished;
  private double initTime = 0;
  private double stopTime = 0;
  private double timeoutTime = 0;
  private double duration = 3;
  private double timeout = Constants.AmpSystemConstants.kNoteTransferTimeoutLimit;

  private Onboarder onboarder;
  private Shooter shooter;
  private AmpSystem ampSystem;

  public AmpOnboard(Onboarder onboarder, Shooter shooter, AmpSystem ampSystem) {
    this.onboarder = onboarder;
    this.shooter = shooter;
    this.ampSystem = ampSystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(onboarder, shooter, ampSystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isFinished = false;
    initTime = Timer.getFPGATimestamp();
    stopTime = Timer.getFPGATimestamp() + duration;
    timeoutTime = Timer.getFPGATimestamp() + timeout;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (ampSystem.getAmpSensor() && initTime >= stopTime) {
      onboarder.setOnboarder(0);
      shooter.setShooter(Constants.ShooterConstants.kShooterLOWPowerValue);
      ampSystem.setRoller(0);
      isFinished = true;
    } else {
      onboarder.setOnboarder(1);
      shooter.setShooter(Constants.ShooterConstants.kShooterLOWPowerValue);
      ampSystem.setRoller(1);
    }

    if (initTime >= timeoutTime) {
      ampSystem.setActuate(0);
      ampSystem.disableMotor();
      throw new Error("AmpOnboard has exceeded timeout limit");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    onboarder.setOnboarder(0);
    shooter.setShooter(0);
    ampSystem.setRoller(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}