// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ShooterCommands.CommandCargoIn;
import frc.robot.commands.ShooterCommands.CommandCargoOut;
import frc.robot.commands.ShooterCommands.CommandCargoStop;
import frc.robot.commands.ShooterCommands.CommandIntakeUp;
import frc.robot.commands.ShooterCommands.CommandIntakeDown;
import frc.robot.commands.ShooterCommands.CommandIntakeStop;
import frc.robot.subsystems.Intake;
import frc.robot.triggers.LeftTrigger;

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
  private LeftTrigger leftTrigger = new LeftTrigger();
  private Trigger rightTrigger = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value)>0.3);
  private JoystickButton aButton = new JoystickButton(xboxController, XboxController.Button.kA.value);
  private JoystickButton yButton = new JoystickButton(xboxController, XboxController.Button.kY.value);
  private final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
  private final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
  private final JoystickButton xButton = new JoystickButton(xboxController, XboxController.Button.kX.value);
  private final JoystickButton bButton = new JoystickButton(xboxController, XboxController.Button.kB.value);
  
  // Instantiate subsystems
  private final Intake intake = new Intake();
  private Shooter shooter = new Shooter();

  // Instantiate Intake commands
  private CommandCargoIn commandCargoIn = new CommandCargoIn(intake);
  private CommandCargoOut commandCargoOut = new CommandCargoOut(intake);
  private CommandCargoStop commandCargoStop = new CommandCargoStop(intake);
  private CommandIntakeUp commandIntakeUp = new CommandIntakeUp(intake);
  private CommandIntakeDown commandIntakeDown = new CommandIntakeDown(intake);
  private CommandIntakeStop commandIntakeStop = new CommandIntakeStop(intake);
  //to do list: add stop commands #, assign commands to button, update spreadsheet, ask meadow about encoders

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