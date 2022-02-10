// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.triggers.LeftTrigger;
import frc.robot.commands.ClimberCommands.CommandAdvance;
import frc.robot.commands.ClimberCommands.CommandArticulate;
import frc.robot.commands.ClimberCommands.CommandClimb;
import frc.robot.commands.ClimberCommands.CommandDecreaseClimberSpeed;
import frc.robot.commands.ClimberCommands.CommandDescend;
import frc.robot.commands.ClimberCommands.CommandIncreaseClimberSpeed;
import frc.robot.commands.ClimberCommands.CommandStopAdvance;
import frc.robot.commands.ClimberCommands.CommandStopArticulate;
import frc.robot.commands.ClimberCommands.CommandStopClimb;
import frc.robot.commands.IntakeCommands.CommandCargoIn;
import frc.robot.commands.IntakeCommands.CommandCargoOut;
import frc.robot.commands.IntakeCommands.CommandCargoStop;
import frc.robot.commands.IntakeCommands.CommandIntakeDown;
import frc.robot.commands.IntakeCommands.CommandIntakeStop;
import frc.robot.commands.IntakeCommands.CommandIntakeUp;
// imports for shooter commands
import frc.robot.commands.ShooterCommands.CommandActivateShooter;
import frc.robot.commands.ShooterCommands.CommandReverseShooter;
import frc.robot.commands.ShooterCommands.CommandStopShooter;
import frc.robot.commands.ShooterCommands.CommandFarHigh;
import frc.robot.commands.ShooterCommands.CommandCloseLow;
//close high has not been created but drivers say it is a position
//import frc.robot.commands.ShooterCommands.CommandCloseHigh;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private XboxController xboxController = new XboxController(0); 
  
  //initialize buttons
  private final LeftTrigger leftTrigger = new LeftTrigger();
  private final Trigger rightTrigger = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value)>0.3);
  private final JoystickButton aButton = new JoystickButton(xboxController, XboxController.Button.kA.value);
  private final JoystickButton yButton = new JoystickButton(xboxController, XboxController.Button.kY.value);
  private final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
  private final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
  private final JoystickButton xButton = new JoystickButton(xboxController, XboxController.Button.kX.value);
  private final JoystickButton bButton = new JoystickButton(xboxController, XboxController.Button.kB.value);
  private final Trigger dpadUp = new Trigger(() -> xboxController.getPOV() == 0);
  private final Trigger dpadDown = new Trigger(() -> xboxController.getPOV() == 180);
  private final Trigger dpadRight = new Trigger(() -> xboxController.getPOV() == 90);
  private final Trigger dpadLeft = new Trigger(() -> xboxController.getPOV() == 270);
  private final XboxController.Axis articulateAxis = XboxController.Axis.kLeftY;
  private final XboxController.Axis advanceAxis = XboxController.Axis.kRightY;
  private final Trigger articulateTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(articulateAxis.value)) > 0.2);
  private final Trigger advanceTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(advanceAxis.value)) > 0.2);

  
  // Instantiate subsystems
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final Climber climber = new Climber(xboxController, articulateAxis, advanceAxis);
  private final DriveBase driveBase = new DriveBase();

  // Instantiate Intake commands
  private final CommandCargoIn commandCargoIn = new CommandCargoIn(intake);
  private final CommandCargoOut commandCargoOut = new CommandCargoOut(intake);
  private final CommandCargoStop commandCargoStop = new CommandCargoStop(intake);
  private final CommandIntakeUp commandIntakeUp = new CommandIntakeUp(intake);
  private final CommandIntakeDown commandIntakeDown = new CommandIntakeDown(intake);
  private final CommandIntakeStop commandIntakeStop = new CommandIntakeStop(intake);
  //to do list: add stop commands #, assign commands to button, update spreadsheet, ask meadow about encoders
  
  // Instantiate Climber Commands
  private final CommandClimb commandClimb = new CommandClimb(climber);
  private final CommandDescend commandDescend = new CommandDescend(climber);
  private final CommandStopClimb commandStopClimb = new CommandStopClimb(climber);
  private final CommandIncreaseClimberSpeed commandIncreaseClimberSpeed = new CommandIncreaseClimberSpeed(climber);
  private final CommandDecreaseClimberSpeed commandDecreaseClimberSpeed = new CommandDecreaseClimberSpeed(climber);
  private final CommandArticulate commandArticulate = new CommandArticulate(climber);
  private final CommandAdvance commandAdvance = new CommandAdvance(climber);
  private final CommandStopArticulate commandStopArticulate = new CommandStopArticulate(climber);
  private final CommandStopAdvance commandStopAdvance = new CommandStopAdvance(climber);

  // Instantiate shooter commands
  private final CommandActivateShooter commandActivateShooter = new CommandActivateShooter(shooter);
  private final CommandStopShooter commandStopShooter = new CommandStopShooter(shooter);
  private final CommandReverseShooter commandReverseShooter = new CommandReverseShooter(shooter);
  private final CommandFarHigh commandFarHigh = new CommandFarHigh(shooter);
  private final CommandCloseLow commandCloseLow = new CommandCloseLow(shooter);
  //close high has not been created but drivers say it is a position
  //private final CommandCloseLow commandCloseHigh = new CommandCloseLow(shooter);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();
  }

  private void configureButtonBindings() {

    // Shooter:
    rightBumper.whileHeld(commandReverseShooter)
                .whenReleased(commandStopShooter);
    leftBumper.whileHeld(commandActivateShooter)
                .whenReleased(commandStopShooter);
    // Speed for shooter
    xButton.whenPressed(commandFarHigh);
    bButton.whenPressed(commandCloseLow);  
    //close high has not been created but drivers say it is a position
    //aButton.whenPressed(commandCloseHigh);  

    // Intake Commands
    leftTrigger.whileActiveContinuous(commandCargoIn)
      .whenInactive(commandCargoStop);
    rightTrigger.whileActiveContinuous(commandCargoOut)
      .whenInactive(commandCargoStop);

    aButton.whileHeld(commandIntakeUp)
        .whenReleased(commandIntakeStop);
    yButton.whileHeld(commandIntakeDown)
        .whenReleased(commandIntakeStop);

    // Climber commands
    dpadUp.whileActiveContinuous(commandClimb)
          .whenInactive(commandStopClimb);
    dpadDown.whileActiveContinuous(commandDescend)
          .whenInactive(commandStopClimb);
    dpadRight.whenActive(commandIncreaseClimberSpeed);
    dpadLeft.whenActive(commandDecreaseClimberSpeed);

    articulateTrigger.whileActiveContinuous(commandArticulate)
          .whenInactive(commandStopArticulate);
    advanceTrigger.whileActiveContinuous(commandAdvance)
          .whenInactive(commandStopAdvance);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }
}