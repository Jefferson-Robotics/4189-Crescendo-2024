// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.DriveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SwerveJoysticks extends PIDCommand {
  /** Creates a new SwerveJoysticks. */
  public SwerveJoysticks(DriveSubsystem swerve, Joystick leftStick, Joystick rightStick) {
    super(
        // The controller that the command will use
        new PIDController(0, 0, 0),
        // This should return the measurement
        () -> swerve.getAngle(),
        // This should return the setpoint (can also be a constant)
        () -> swerve.getChosenAngle(),
        // This uses the output
        output -> {
          double xSpeed = MathUtil.applyDeadband(leftStick.getY(), OIConstants.kDriveDeadband);
          double ySpeed = MathUtil.applyDeadband(leftStick.getX(), OIConstants.kDriveDeadband);
          double rotationSpeed = MathUtil.applyDeadband(rightStick.getX(), OIConstants.kDriveDeadband);
          //if(!(-MathUtil.applyDeadband(rightShaft.getX() * -.5, OIConstants.kDriveDeadband) > 0)){        
          //  drive.setChosenAngle(output + Constants.DriveConstants.HeadingTurnRate);
         // }
          // Use the output here
          //drive.drive(xSpeed, ySpeed, output, true, true);
          if(xSpeed == 0 && ySpeed==0 && rotationSpeed == 0){
            swerve.setX();
          } else {
            swerve.drive(xSpeed, ySpeed, rotationSpeed, true, true);
          }
        });
        //absolute angle is tan inverse
        addRequirements(swerve);
    SmartDashboard.putNumber("P", 0);
    SmartDashboard.putNumber("I",0);
    SmartDashboard.putNumber("D",0);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
