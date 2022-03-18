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
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Intake;
import frc.robot.triggers.LeftTrigger;
import frc.robot.Constants.ClimberConfig;
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
//Intake Commands
import frc.robot.commands.IntakeCommands.CommandCargoIn;
import frc.robot.commands.IntakeCommands.CommandCargoOut;
import frc.robot.commands.IntakeCommands.CommandCargoStop;
import frc.robot.commands.IntakeCommands.CommandIntakeDown;
import frc.robot.commands.IntakeCommands.CommandIntakeStop;
import frc.robot.commands.IntakeCommands.CommandIntakeUp;
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
  private boolean okToContinueDescend = false;
  private boolean isFullyDescended = false;
  //initialize buttons
  private final LeftTrigger leftTrigger = new LeftTrigger();
  private final Trigger rightTrigger = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightTrigger.value)>0.3);
  private final JoystickButton aButton = new JoystickButton(xboxController, XboxController.Button.kA.value);
  private final JoystickButton yButton = new JoystickButton(xboxController, XboxController.Button.kY.value);
  private final JoystickButton rightBumper = new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
  private final JoystickButton leftBumper = new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
  private final JoystickButton xButton = new JoystickButton(xboxController, XboxController.Button.kX.value);
  private final JoystickButton bButton = new JoystickButton(xboxController, XboxController.Button.kB.value);

  private final JoystickButton back = new JoystickButton(xboxController, XboxController.Button.kBack.value);
  private final JoystickButton start = new JoystickButton(xboxController, XboxController.Button.kStart.value);


  private final JoystickButton stick7 = new JoystickButton(stick, 7);
  private final JoystickButton stick8 = new JoystickButton(stick, 8);
  private final JoystickButton stick9 = new JoystickButton(stick, 9);
  private final JoystickButton stick10 = new JoystickButton(stick, 10);
  private final JoystickButton stick11 = new JoystickButton(stick, 11);
  private final JoystickButton stick12 = new JoystickButton(stick, 12);

  private final Trigger leftStickUp = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kLeftY.value) < -0.3);
  private final Trigger leftStickDown = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kLeftY.value) > 0.3);


  private final Trigger dpadUp = new Trigger(() -> xboxController.getPOV() == 0);
  private final Trigger dpadDown = new Trigger(() -> xboxController.getPOV() == 180);
  private final Trigger dpadRight = new Trigger(() -> xboxController.getPOV() == 90);
  private final Trigger dpadLeft = new Trigger(() -> xboxController.getPOV() == 270);
  private final Trigger triggerClearSolenoid = new Trigger(() -> leftStickUp.get() && !okToContinueDescend && !isFullyDescended);
  private final Trigger triggerContinueDescend = new Trigger(() -> leftStickUp.get() && okToContinueDescend);

  

  private final Trigger rightStickUp = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightY.value) < -0.3);
  private final Trigger rightStickDown = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightY.value) > 0.3);

  

  // TODO: These need to be re-assigned to Primary Joystick
  // private final XboxController.Axis tiltAxis = XboxController.Axis.kLeftY;
  // private final XboxController.Axis outerArmAxis = XboxController.Axis.kRightY;
  // private final Trigger articulateTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(tiltAxis.value)) > 0.2);
  // private final Trigger advanceTrigger = new Trigger(() -> Math.abs(xboxController.getRawAxis(outerArmAxis.value)) > 0.2);

  
  // TODO: Update the axis when instantiating Climber
  //TODO list: add stop commands #, assign commands to button, update spreadsheet, ask meadow about encoders
  // Instantiate subsystems
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  public final Climber climber = new Climber();
  private final DriveBase driveBase = new DriveBase();

  private final Trigger triggerOuterArmsTooHigh = new Trigger(() -> climber.getAreOuterArmsTooLong());


  // Instantiate Intake commands
  private final CommandCargoIn commandCargoIn = new CommandCargoIn(intake);
  private final CommandCargoOut commandCargoOut = new CommandCargoOut(intake);
  private final CommandCargoStop commandCargoStop = new CommandCargoStop(intake);
  private final CommandIntakeUp commandIntakeUp = new CommandIntakeUp(intake);
  private final CommandIntakeDown commandIntakeDown = new CommandIntakeDown(intake);
  private final CommandIntakeStop commandIntakeStop = new CommandIntakeStop(intake);
  
  // Instantiate Climber Commands
  private final CommandClimb commandClimb = new CommandClimb(climber, this);
  private final CommandContinueDescend commandContinueDescend = new CommandContinueDescend(climber, this);
  private final CommandStopClimb commandStopClimb = new CommandStopClimb(climber);
  private final CommandIncreaseClimberSpeed commandIncreaseClimberSpeed = new CommandIncreaseClimberSpeed(climber);
  private final CommandDecreaseClimberSpeed commandDecreaseClimberSpeed = new CommandDecreaseClimberSpeed(climber);
  private final CommandExtendOuterArms commandExtendOuterArms = new CommandExtendOuterArms(climber);
  private final CommandStartTilt commandStartTilt = new CommandStartTilt(climber);
  private final CommandStopTilt commandStopTilt = new CommandStopTilt(climber);
  private final CommandFullTilt commandFullTilt = new CommandFullTilt(climber);
  private final CommandFullTiltRetract commandFullTiltRetract = new CommandFullTiltRetract(climber);

  private final CommandRetractTilt commandRetractTilt = new CommandRetractTilt(climber);
  private final CommandStopOuterArms commandStopOuterArms = new CommandStopOuterArms(climber);
  private final CommandSolenoid commandSolenoid = new CommandSolenoid(climber);
  private final CommandStopSolenoid commandStopSolenoid = new CommandStopSolenoid(climber);

  private final CommandRetractOuterArms commandRetractOuterArms = new CommandRetractOuterArms(climber);

  private final CommandSolenoidAscend commandSolenoidAscend = new CommandSolenoidAscend(climber);
  private final CommandSolenoidDescend commandSolenoidDescend = new CommandSolenoidDescend(climber, this);

  //private final SequentialCommandGroup commandGroupSolenoidDescend = new SequentialCommandGroup(commandStopSolenoid, commandSolenoidAscend, commandDescend);
  private final SequentialCommandGroup commandGroupSolenoidDescend = new SequentialCommandGroup(commandStopSolenoid, commandSolenoidAscend, commandSolenoidDescend);

  // Instantiate Shooter commands
  private final CommandActivateShooter commandActivateShooter = new CommandActivateShooter(shooter);
  private final CommandStopShooter commandStopShooter = new CommandStopShooter(shooter);
  private final CommandReverseShooter commandReverseShooter = new CommandReverseShooter(shooter);
  private final CommandFarHigh commandFarHigh = new CommandFarHigh(shooter);
  private final CommandCloseLow commandCloseLow = new CommandCloseLow(shooter);
  private final CommandStartFeeder commandStartFeeder = new CommandStartFeeder(shooter);
  private final CommandStopFeeder commandStopFeeder = new CommandStopFeeder(shooter);

  private final DriveCommands commandDrive = new DriveCommands(driveBase, stick);

  //close high has not been created but drivers say it is a position
  //private final CommandCloseLow commandCloseHigh = new CommandCloseLow(shooter);

  //Instantiate Auton commands
  
  private final AutonStartShooterCommand autonStartShooterCommand = new AutonStartShooterCommand(shooter);
  private final SequentialCommandGroup autonDriveShootCG = new SequentialCommandGroup(new AutonDriveCommand(driveBase, 1.5, 0.2), new AutonStartShooterCommand(shooter), new CommandStartFeeder(shooter), new AutonShooterCommand(shooter, intake, 1.5));
  private final SequentialCommandGroup autonFullRoutineCG = new SequentialCommandGroup(new AutonDriveCommand(driveBase, 1.5, 0.2),  
  autonStartShooterCommand, commandStartFeeder, new AutonShooterCommand(shooter, intake, 1.5), new AutonIntakeDownCommand(intake) 
  ,new AutonCargoCommand(intake), new AutonDriveCommand(driveBase, 2.0, 0.2),new AutonDriveCommand(driveBase, 2.0, -0.2), 
  new AutonStartShooterCommand(shooter), new CommandStartFeeder(shooter), new AutonShooterCommand(shooter, intake, 1.5));
  

  //Auton routine chooser
  private final SendableChooser<Command> autonChooser = new SendableChooser<>();
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();
    driveBase.setDefaultCommand(commandDrive);
    //autonChooser.addOption("Drive Forward", autonDriveCommand);
    //autonChooser.setDefaultOption("Drive-Shoot", autonDriveShootCG);
    //autonChooser.addOption("Full-Routine", autonFullRoutineCG);
    autonChooser.setDefaultOption("Full-Routine", autonFullRoutineCG);
    autonChooser.addOption("Drive-Shoot", autonDriveShootCG);
    SmartDashboard.putData(autonChooser);

  }

  public void setOkToDescend(boolean inputValue){
    this.okToContinueDescend = inputValue;
  }
  public boolean getLStickUp(){
    return leftStickUp.get();
  }
  public void setIsFullyDescended(boolean input){
    this.isFullyDescended = input;
  }

  private void configureButtonBindings() {

    // Shooter:
    aButton.whileHeld(commandReverseShooter)
                .whenReleased(commandStopShooter);
    rightTrigger.whenActive(commandActivateShooter)
                .whenInactive(commandStopShooter);

    leftTrigger.whenActive(new CommandStartFeeder(shooter))
            .whenInactive(new CommandStopFeeder(shooter));
    //leftStickUp.whenActive(commandStartFeeder);
    //leftStickUp.whenInactive(commandStopFeeder);
    //
    
    // Speed for shooter
    back.whenPressed(commandFarHigh);
    start.whenPressed(commandCloseLow);  
  

    // Intake Commands
    rightBumper.whenActive(commandCargoIn)
      .whenInactive(commandCargoStop);
    yButton.whenActive(commandCargoOut)
      .whenInactive(commandCargoStop);

    bButton.whenPressed(commandIntakeUp)
          .whenReleased(commandIntakeStop);
    xButton.whenPressed(commandIntakeDown)
        .whenReleased(commandIntakeStop);

    // Climber commands - Secondary Commands
    //dpadDown.whenActive(commandClimb)
          //.whenInactive(commandStopClimb);//
    leftStickDown.whenActive(commandClimb)
          .whenInactive(commandStopClimb);
    // dpadUp.whenActive(commandGroupSolenoidDescend)
    //       .whenInactive(commandStopClimb);//
    triggerClearSolenoid.whenActive(commandGroupSolenoidDescend, false);
    triggerContinueDescend.whenActive(commandContinueDescend)
          .whenInactive(commandStopClimb);
    dpadRight.whenActive(commandFullTilt);
    dpadLeft.whileActiveContinuous(commandRetractTilt);
    ParallelCommandGroup climbReadyCommandGroup = new ParallelCommandGroup(
        new CommandInnerArmToPosition(climber, ClimberConfig.INNER_CLIMB_READY),
        new CommandOuterArmToPosition(climber, ClimberConfig.OUTER_CLIMB_READY),
        new CommandFullTilt(climber)); 
        
    ParallelCommandGroup climbZeroCommandGroup = new ParallelCommandGroup(
        new CommandInnerArmToPosition(climber, 0),
        new CommandOuterArmToPosition(climber, 0),
        new CommandFullTiltRetract(climber));
      
    dpadUp.whenActive(climbReadyCommandGroup);
    dpadDown.whenActive(climbZeroCommandGroup);
    
    //dpadRight.whileActiveContinuous(commandStartTilt);
    //dpadLeft.whileActiveContinuous(commandRetractTilt);
   // leftStickUp.whenActive(commandFullTilt);
   // leftStickDown.whenActive(commandFullTiltRetract);

    // rightStickDown.whenActive(commandRetractOuterArms)
    //       .whenInactive(commandStopOuterArms);
    Command condCommand = new ConditionalCommand(new CommandRetractOuterArms(climber), new CommandStopOuterArms(climber), ()->rightStickDown.get());
    rightStickDown.whileActiveContinuous(commandRetractOuterArms);
    rightStickUp.whileActiveContinuous(commandExtendOuterArms);
    // rightStickUp.whenActive(commandExtendOuterArms)
    //       .whenInactive(commandStopOuterArms);

    // triggerOuterArmsTooHigh.whenActive(new CommandRetractOuterArmsToLegalLimit(climber));

    // Climber commands - Primary Commands
    /*
    stick7.whenPressed(commandClimb)
        .whenReleased(commandStopClimb);

    stick8.whenPressed(commandGroupSolenoidDescend)
        .whenReleased(commandStopClimb);
    
    stick11.whenPressed(commandExtendOuterArms)
        .whenReleased(commandStopOuterArms);

    stick12.whenPressed(commandRetractOuterArms)
        .whenReleased(commandStopOuterArms);
    */

    
    //TODO: reassign to new Joystick
    // articulateTrigger.whileActiveContinuous(commandArticulate)
    //       .whenInactive(commandStopArticulate);
    // advanceTrigger.whileActiveContinuous(commandAdvance)
    //       .whenInactive(commandStopAdvance);
    
    
  }
   
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    //return autonShootCommandGroup;
    return autonChooser.getSelected();
  }
}