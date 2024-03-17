// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OIConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.RecordPlaybackConstants;
import frc.robot.Constants.ShuffleboardConstants;
import frc.robot.commands.PrimeShooter;
import frc.robot.commands.ShootNote;
import frc.robot.commands.AutoPrime;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.CancelAll;
import frc.robot.commands.DriveActuate;
import frc.robot.commands.DriveClimbDown;
import frc.robot.commands.DriveClimbUp;
import frc.robot.commands.DriveShooter;
import frc.robot.commands.OnboarderSystem;
import frc.robot.commands.PlayBack;
import frc.robot.commands.SwerveJoysticks;
import frc.robot.subsystems.AmpSystem;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Onboarder;
import frc.robot.subsystems.Shooter;

import java.io.File;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The driver's controller
  private CommandXboxController m_operatorController = new CommandXboxController(OIConstants.kDriverControllerPort);
  private Joystick leftStick = new Joystick(OperatorConstants.kDriverJoystickLeft);
  private Joystick rightStick = new Joystick(OperatorConstants.kDriverJoystickRight);

  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final Climb climb = new Climb();
  private final AmpSystem ampSystem = new AmpSystem();
  private final Onboarder onboarder = new Onboarder();
  private final Shooter shooter = new Shooter();

  // The robot's commands
  private CancelAll cancelAll;
  private DriveShooter driveShooter = new DriveShooter(shooter);
  private DriveClimbUp driveClimbUp = new DriveClimbUp(climb);
  private DriveClimbDown driveClimbDown = new DriveClimbDown(climb);

  //private AutoOnboard autoOnboarder = new AutoOnboard(onboarder);
  //private OnboarderSystem onboarderSystem = new OnboarderSystem(onboarder, m_operatorController);
  //private DriveOnboarder driveOnboarder = new DriveOnboarder(onboarder, m_operatorController);
  //private DriveActuate driveAcuate = new DriveActuate(m_operatorController, ampSystem);

  // Autonomous Commands
  private AutoPrime autoPrimeShooter = new AutoPrime(ampSystem, shooter, onboarder);
  private AutoShoot autoShootNote = new AutoShoot(ampSystem, shooter, onboarder);
  //private PrimeShooter primeShooter = new PrimeShooter(shooter, onboarder);
  //private ShootNote shootNote = new ShootNote(shooter, onboarder);

  // Shuffleboard Autonomous Tab
  private final ShuffleboardTab autoTab = Shuffleboard.getTab(ShuffleboardConstants.kAutonomousTab);
  private final GenericEntry alliancebox = autoTab.add("Red Alliance", false).withWidget(BuiltInWidgets.kToggleButton).getEntry();
  private final SendableChooser<File> fileChooser = new SendableChooser<>();
  // Playback
  private PlayBack playBackAuto;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Shuffleboard file access for playback files
    File[] files = RecordPlaybackConstants.kRecordDirectory.listFiles();
    for (int i = 0; i < files.length; i++) {
      fileChooser.addOption(files[i].getName(), files[i]);
    }
    autoTab.add("Autonomous Mode", fileChooser)
    .withWidget(BuiltInWidgets.kComboBoxChooser)
    .withPosition(0, 0)
    .withSize(2, 1);

    // Configure the trigger bindings
    configureBindings();

    // Configure default commands
    onboarder.setDefaultCommand(new OnboarderSystem(onboarder, m_operatorController, false, false));
    ampSystem.setDefaultCommand(new DriveActuate(m_operatorController, ampSystem));
    m_robotDrive.setDefaultCommand(new SwerveJoysticks(m_robotDrive, leftStick, rightStick));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // OPERATOR BUTTONS
    cancelAll = new CancelAll(m_robotDrive, onboarder, shooter, climb, ampSystem);
    m_operatorController.button(5).onTrue(cancelAll);

    m_operatorController.leftBumper().onTrue(autoPrimeShooter);
    m_operatorController.rightBumper().onTrue(autoShootNote);

    // Manual Control
    m_operatorController.back().onTrue(driveShooter);
    // WARNING: Manual Onboarder override is located in OnboarderSystem.java
    m_operatorController.povUp().onTrue(driveClimbUp);
    m_operatorController.povUpLeft().onTrue(driveClimbUp);
    m_operatorController.povUpRight().onTrue(driveClimbUp);
    m_operatorController.povDown().onTrue(driveClimbDown);
    m_operatorController.povDownLeft().onTrue(driveClimbDown);
    m_operatorController.povDownRight().onTrue(driveClimbDown);

    // DRIVER BUTTONS
    playBackAuto = new PlayBack(m_robotDrive, onboarder, shooter, fileChooser, alliancebox);
    new JoystickButton(rightStick, 7).onTrue(playBackAuto);
    new JoystickButton(leftStick, 6).onTrue(cancelAll);
    new JoystickButton(leftStick, 7).onTrue(cancelAll);

    new JoystickButton(leftStick, 1).onTrue(new OnboarderSystem(onboarder, m_operatorController, true, true));
    new JoystickButton(rightStick, 1).onTrue(new OnboarderSystem(onboarder, m_operatorController, true, false));
    new JoystickButton(rightStick, 3).onTrue(driveShooter);

    new JoystickButton(leftStick, 10).whileTrue(new RunCommand(
        () -> {
          m_robotDrive.resetGyro();
        }
    ));
    new JoystickButton(leftStick, 11).whileTrue(new RunCommand(
        () -> {
          m_robotDrive.resetGyro();
        }
    ));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Run currently selected playback file
    return new PlayBack(m_robotDrive, onboarder, shooter, fileChooser, alliancebox);
  }
}
