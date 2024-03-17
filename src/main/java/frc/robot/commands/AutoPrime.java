// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.AmpSystem;
import frc.robot.subsystems.Onboarder;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoPrime extends SequentialCommandGroup {
  /** Creates a new AutoPrime. */

  public AutoPrime(AmpSystem ampSystem, Shooter shooter, Onboarder onboarder) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new ActuateToAmp(ampSystem), new PrimeShooter(shooter, onboarder));
  }
}
