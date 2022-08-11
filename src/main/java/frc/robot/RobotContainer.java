// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.swing.JToggleButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.triggers.LeftTrigger;
import frc.robot.Constants.ClimberConfig;
import frc.robot.utils.DoubleButton;
import frc.robot.utils.SingleButton;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.AutonCommands.AutonCargoCommand;
import frc.robot.commands.AutonCommands.AutonDriveCommand;
import frc.robot.commands.AutonCommands.AutonIntakeDownCommand;
import frc.robot.commands.AutonCommands.AutonIntakeUpCommand;
import frc.robot.commands.AutonCommands.AutonShooterCommand;
import frc.robot.commands.AutonCommands.AutonStartShooterCommand;
//Climber Commands
import frc.robot.commands.ClimberCommands.CommandExtendOuterArms;
import frc.robot.commands.ClimberCommands.CommandFullTilt;
import frc.robot.commands.ClimberCommands.CommandFullTiltRetract;
import frc.robot.commands.ClimberCommands.CommandStartTilt;
import frc.robot.commands.ClimberCommands.CommandDecreaseClimberSpeed;
import frc.robot.commands.ClimberCommands.CommandClimb;
import frc.robot.commands.ClimberCommands.CommandContinueDescend;
import frc.robot.commands.ClimberCommands.CommandIncreaseClimberSpeed;
import frc.robot.commands.ClimberCommands.CommandInnerArmToPosition;
import frc.robot.commands.ClimberCommands.CommandMoveOuterArmsVariableSpeed;
import frc.robot.commands.ClimberCommands.CommandOuterArmToPosition;
import frc.robot.commands.ClimberCommands.CommandRetractOuterArms;
import frc.robot.commands.ClimberCommands.CommandRetractOuterArmsToLegalLimit;
import frc.robot.commands.ClimberCommands.CommandRetractTilt;
import frc.robot.commands.ClimberCommands.CommandStopOuterArms;
import frc.robot.commands.ClimberCommands.CommandStopTilt;
import frc.robot.commands.ClimberCommands.CommandStopClimb;
import frc.robot.commands.ClimberCommands.CommandStopSolenoid;
import frc.robot.commands.ClimberCommands.CommandSolenoid;
import frc.robot.commands.ClimberCommands.CommandSolenoidAscend;
import frc.robot.commands.ClimberCommands.CommandSolenoidDescend;
//Shooter Commands
import frc.robot.commands.ShooterCommands.CommandActivateShooter;
import frc.robot.commands.ShooterCommands.CommandReverseShooter;
import frc.robot.commands.ShooterCommands.CommandStartFeeder;
import frc.robot.commands.ShooterCommands.CommandStopFeeder;
import frc.robot.commands.ShooterCommands.CommandStopShooter;
import frc.robot.commands.ShooterCommands.CommandFarHigh;
import frc.robot.commands.ShooterCommands.CommandCloseLow;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private XboxController xboxController = new XboxController(0); 
  private Joystick stick = new Joystick(1);

  

  // TODO: These need to be re-assigned to Primary Joystick
  // private final XboxController.Axis tiltAxis = XboxController.Axis.kLeftY;
  // private final XboxController.Axis outerArmAxis = XboxController.Axis.kRightY;
  // private final Trigger articulateTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(tiltAxis.value)) > 0.2);
  // private final Trigger advanceTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(outerArmAxis.value)) > 0.2);

  
  // TODO: Update the axis when instantiating Climber
  //TODO list: add stop commands #, assign commands to button, update spreadsheet, ask meadow about encoders
  // Instantiate subsystems
  private final DriveBase driveBase = new DriveBase();

  private final DriveCommands commandDrive = new DriveCommands(driveBase, stick);

  //close high has not been created but drivers say it is a position
  //private final CommandCloseLow commandCloseHigh = new CommandCloseLow(shooter);

   /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();
    driveBase.setDefaultCommand(commandDrive);
    
  }


  private void configureButtonBindings() {

    
  }
   
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    //return autonShootCommandGroup;
    return null;
  }
}