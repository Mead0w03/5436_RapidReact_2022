// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.ShooterCommands.CommandCargoIn;
import frc.robot.commands.ShooterCommands.CommandCargoOut;
import frc.robot.commands.ShooterCommands.CommandCargoStop;
import frc.robot.commands.ShooterCommands.CommandIntakeUp;
import frc.robot.commands.ShooterCommands.CommandIntakeDown;
import frc.robot.commands.ShooterCommands.CommandIntakeStop;
import frc.robot.subsystems.Intake;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final Intake intake = new Intake();
  private XboxController xboxController = new XboxController(0);

  //intake subsystems and commands
  private JoystickButton ltButton = new JoystickButton(xboxController, XboxController.Button.kLeftStick.value);
  private JoystickButton xButton = new JoystickButton(xboxController, XboxController.Button.kX.value);
  private CommandCargoIn commandCargoIn = new CommandCargoIn(intake);
  private CommandCargoOut commandCargoOut = new CommandCargoOut(intake);
  private CommandCargoStop commandCargoStop = new CommandCargoStop(intake);
  //private CommandIntakeIn commandIntakeIn = new CommandIntakeIn();
  //private CommandIntakeOut commandIntakeOut = new CommandIntakeOut();
  //to do list: add stop commands #, assign commands to button, update spreadsheet, ask meadow about encoders

  private JoystickButton aButton = new JoystickButton(xboxController, XboxController.Button.kA.value);
  private JoystickButton bButton = new JoystickButton(xboxController, XboxController.Button.kB.value);
  private CommandIntakeUp commandIntakeUp = new CommandIntakeUp(intake);
  private CommandIntakeDown commandIntakeDown = new CommandIntakeDown(intake);
  private CommandIntakeStop commandIntakeStop = new CommandIntakeStop(intake);



  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    ltButton.whileHeld(commandCargoIn)
          .whenReleased(commandCargoStop);
    xButton.whileHeld(commandCargoOut)
          .whenReleased(commandCargoStop);

    aButton.whileHeld(commandIntakeUp)
          .whenReleased(commandIntakeStop);
    bButton.whileHeld(commandIntakeDown)
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
