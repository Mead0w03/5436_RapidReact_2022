// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.swing.JToggleButton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
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
import frc.robot.subsystems.ClimberTilt;
import frc.robot.subsystems.ClimberOuter;
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
import frc.robot.commands.ClimberCommands.CommandClimbAscend;
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
  private final SingleButton bButtonAlone = new SingleButton(bButton, leftBumper);
  private final SingleButton xButtonAlone = new SingleButton(xButton, leftBumper);
  private final DoubleButton bAndLeftBumper = new DoubleButton(bButton, leftBumper);
  private final DoubleButton xAndLeftBumper = new DoubleButton(xButton, leftBumper);

  private final JoystickButton back = new JoystickButton(xboxController, XboxController.Button.kBack.value);
  private final JoystickButton start = new JoystickButton(xboxController, XboxController.Button.kStart.value);


  private final JoystickButton stick7 = new JoystickButton(stick, 7);
  private final JoystickButton stick8 = new JoystickButton(stick, 8);
  private final JoystickButton stick9 = new JoystickButton(stick, 9);
  private final JoystickButton stick10 = new JoystickButton(stick, 10);
  private final JoystickButton stick11 = new JoystickButton(stick, 11);
  private final JoystickButton stick12 = new JoystickButton(stick, 12);

  private final Trigger leftStickUp = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kLeftY.value) < -0.7);
  private final Trigger leftStickDown = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kLeftY.value) > 0.7);


  private final Trigger dpadUp = new Trigger(() -> xboxController.getPOV() == 0);
  private final Trigger dpadDown = new Trigger(() -> xboxController.getPOV() == 180);
  private final Trigger dpadRight = new Trigger(() -> xboxController.getPOV() == 90 || xboxController.getPOV() == 45 || xboxController.getPOV() == 135);
  private final Trigger dpadLeft = new Trigger(() -> xboxController.getPOV() == 270 || xboxController.getPOV() == 315 || xboxController.getPOV() == 225);
  // private final Trigger triggerClearSolenoid = new Trigger(() -> leftStickUp.get() && !okToContinueDescend && !isFullyDescended);
  // private final Trigger triggerContinueDescend = new Trigger(() -> leftStickUp.get() && okToContinueDescend);

  

  // private final Trigger rightStickUp = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightY.value) < -0.3);
  // private final Trigger rightStickUp = new Trigger(() -> xboxController.getRawAxis(XboxController.Axis.kRightY.value) < -0.3);
  private final Trigger rightStickEngaged = new Trigger(() -> Math.abs(xboxController.getRawAxis(XboxController.Axis.kRightY.value)) > 0.3);

  

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
  public final ClimberTilt climberTilt = new ClimberTilt();
  public final ClimberOuter climberOuter = new ClimberOuter();
  private final DriveBase driveBase = new DriveBase();

  // private final Trigger triggerOuterArmsTooHigh = new Trigger(() -> climberOuter.getAreOuterArmsTooLong());


  // Instantiate Intake commands
  private final RunCommand commandCargoIn = new RunCommand(intake::cargoIn,intake);
  private final RunCommand commandCargoOut = new RunCommand(intake::cargoOut, intake);
  private final InstantCommand commandCargoStop = new InstantCommand(intake::cargoStop, intake);
  private final RunCommand commandIntakeUpManual = new RunCommand(()-> intake.intakeMove("Up", "Manual"), intake);
  private final RunCommand commandIntakeDownManual = new RunCommand(()-> intake.intakeMove("Down", "Manual"), intake);
  private final RunCommand commandIntakeUpPID = new RunCommand(()-> intake.intakeMove("Up", "PID"), intake);
  //private final RunCommand commandIntakeDownPID = new RunCommand(()-> intake.intakeMove("Down", "PID"), intake);
  //private final InstantCommand commandChangeIntakeMode = new InstantCommand(intake::changeIntakeMode, intake);
  private final InstantCommand commandIntakeStop = new InstantCommand(intake::intakeStop, intake);
  private final InstantCommand commandResetIntakeEncoders = new InstantCommand(intake::resetRetractEncoders, intake);
  //private final ConditionalCommand commandIntakeUp = new ConditionalCommand(commandIntakeUpPID, commandIntakeUpManual, intake.isRetractModePID());
  //private final ConditionalCommand commandIntakeDown = new ConditionalCommand(commandIntakeDownPID, commandIntakeDownManual, intake.isRetractModePID());
  
  // Instantiate Climber Commands
  private final CommandClimb commandClimbDescend = new CommandClimb(climber, this);
  private final CommandClimbAscend commandClimbAscend = new CommandClimbAscend(climber);
  private final CommandContinueDescend commandContinueDescend = new CommandContinueDescend(climber, this);
  private final CommandStopClimb commandStopClimb = new CommandStopClimb(climber);
  private final CommandIncreaseClimberSpeed commandIncreaseClimberSpeed = new CommandIncreaseClimberSpeed(climber);
  private final CommandDecreaseClimberSpeed commandDecreaseClimberSpeed = new CommandDecreaseClimberSpeed(climber);
  private final CommandExtendOuterArms commandExtendOuterArms = new CommandExtendOuterArms(climberOuter);
  private final CommandStartTilt commandStartTilt = new CommandStartTilt(climberTilt);
  private final CommandStopTilt commandStopTilt = new CommandStopTilt(climberTilt);
  private final CommandFullTilt commandFullTilt = new CommandFullTilt(climberTilt);
  private final CommandFullTiltRetract commandFullTiltRetract = new CommandFullTiltRetract(climberTilt);

  private final CommandRetractTilt commandRetractTilt = new CommandRetractTilt(climberTilt);
  private final CommandStopOuterArms commandStopOuterArms = new CommandStopOuterArms(climberOuter);

  private final CommandRetractOuterArms commandRetractOuterArms = new CommandRetractOuterArms(climberOuter);

  //private final SequentialCommandGroup commandGroupSolenoidDescend = new SequentialCommandGroup(commandStopSolenoid, commandSolenoidAscend, commandDescend);

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
  
  private final AutonStartShooterCommand autonStartShooterCommand = new AutonStartShooterCommand(shooter, 2.0);
  private final SequentialCommandGroup autonDriveShootCG = new SequentialCommandGroup(new AutonDriveCommand(driveBase, 1.5, 0.2), 
  new AutonStartShooterCommand(shooter, 2.0), 
  new AutonShooterCommand(shooter, intake, 1.5));
  private final SequentialCommandGroup autonFullRoutineCG = new SequentialCommandGroup(new AutonIntakeDownCommand(intake),
  new AutonCargoCommand(intake),
  new AutonDriveCommand(driveBase, 2.0, 0.2),   
  new AutonStartShooterCommand(shooter, 2.0), 
  new AutonDriveCommand(driveBase, 1.0, -0.2), new AutonShooterCommand(shooter, intake, 3.0));
  private final SequentialCommandGroup autonWallRoutine = new SequentialCommandGroup(new AutonIntakeDownCommand(intake),
  new AutonCargoCommand(intake), new AutonDriveCommand(driveBase, 0.6, 0.2), new AutonStartShooterCommand(shooter, 2.0),
  new AutonDriveCommand(driveBase, 0.6, -0.2), new AutonShooterCommand(shooter, intake, 3.0), new InstantCommand(()->intake.intakeMove("Up","PID")),
  new AutonDriveCommand(driveBase, 1.0, 0.2));

  //Auton routine chooser
  private final SendableChooser<Command> autonChooser = new SendableChooser<>();
  /** The container for the robot. Contains subsystems, OI devices, and commands. */


  // PDP

 
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();
    driveBase.setDefaultCommand(commandDrive);
    //autonChooser.addOption("Drive Forward", autonDriveCommand);
    //autonChooser.setDefaultOption("Drive-Shoot", autonDriveShootCG);
    //autonChooser.addOption("Full-Routine", autonFullRoutineCG);
    autonChooser.setDefaultOption("Full-Routine", autonFullRoutineCG);
    autonChooser.addOption("Drive-Shoot", autonDriveShootCG);
    autonChooser.addOption("Wall Routine", autonWallRoutine);
    SmartDashboard.putData(autonChooser);
    SmartDashboard.putData(new CommandFullTilt(climberTilt));

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

    xAndLeftBumper.whenPressed(commandResetIntakeEncoders);
/*
    bAndLeftBumper.whenPressed(commandChangeIntakeMode);
*/

/*
    bButtonAlone.whenPressed(commandIntakeUp)
      .whenReleased(commandIntakeStop);
*/
    bButtonAlone.whenPressed(commandIntakeUpManual)
      .whenReleased(commandIntakeStop);
    xButtonAlone.whenPressed(commandIntakeDownManual)
      .whenReleased(commandIntakeStop); 

    // Climber commands - Secondary Commands
    leftStickDown.whenActive(commandClimbDescend)
          .whenInactive(commandStopClimb);
    leftStickUp.whenActive(commandClimbAscend)
          .whenInactive(commandStopClimb);
    
    // Inner Climber Commands
    
    
    // Outer Climber
    // rightStickDown.whileActiveContinuous(commandRetractOuterArms);
    // rightStickUp.whileActiveContinuous(commandExtendOuterArms);
    // rightStickUp.whileActiveContinuous(new CommandMoveOuterArmsVariableSpeed(climber, () -> xboxController.getRawAxis(XboxController.Axis.kRightY.value)));
    // rightStickDown.whileActiveContinuous(new CommandMoveOuterArmsVariableSpeed(climber, () -> xboxController.getRawAxis(XboxController.Axis.kRightY.value)));
    rightStickEngaged.whileActiveContinuous(new CommandMoveOuterArmsVariableSpeed(climberOuter, () -> xboxController.getRawAxis(XboxController.Axis.kRightY.value)));
    
    //xboxController.getRawAxis(XboxController.Axis.kLeftY.value)

    // Tilt
    // dpadUp.whenActive(commandFullTilt);
    dpadLeft.whileActiveContinuous(commandRetractTilt);
    dpadRight.whileActiveContinuous(commandStartTilt);

    // Suto climb Commands
    // ParallelCommandGroup climbReadyCommandGroup = new ParallelCommandGroup(
    //     new CommandInnerArmToPosition(climber, ClimberConfig.INNER_CLIMB_READY),
    //     new CommandOuterArmToPosition(climber, ClimberConfig.OUTER_CLIMB_READY),
    //     new CommandFullTilt(climber)); 
        
    // ParallelCommandGroup climbZeroCommandGroup = new ParallelCommandGroup(
    //     new CommandInnerArmToPosition(climber, 0),
    //     new CommandOuterArmToPosition(climber, 0),
    //     new CommandFullTiltRetract(climber));

    ParallelCommandGroup enterClimbModeCommandGroup = new ParallelCommandGroup(
        new CommandInnerArmToPosition(climber, ClimberConfig.INNER_ENTER_CLIMB),
        new CommandOuterArmToPosition(climberOuter, ClimberConfig.OUTER_ENTER_CLIMB),
        new CommandFullTilt(climberTilt) 
    );
    SmartDashboard.putData("Enter Climb Mode", enterClimbModeCommandGroup);

    ParallelCommandGroup prepClimbMidRungCommandGroup = new ParallelCommandGroup(
      new CommandInnerArmToPosition(climber, ClimberConfig.INNER_PREP_MID),
      new CommandOuterArmToPosition(climberOuter, ClimberConfig.OUTER_REACH_MID),
      new CommandFullTilt(climberTilt)
    );
    SmartDashboard.putData("Prep Climb Mode", prepClimbMidRungCommandGroup);
      
    ParallelCommandGroup climbMidRungCommandGroup = new ParallelCommandGroup(
      new CommandInnerArmToPosition(climber, ClimberConfig.INNER_CLIMB_MID),
      new CommandOuterArmToPosition(climberOuter, ClimberConfig.OUTER_REACH_MID),
      new CommandFullTilt(climberTilt)
    );
    SmartDashboard.putData("Climb Middle Rung", climbMidRungCommandGroup);

    ParallelCommandGroup advanceHighRungCommandGroup = new ParallelCommandGroup(
      new CommandInnerArmToPosition(climber, ClimberConfig.INNER_ADVANCE_HIGH),
      new CommandOuterArmToPosition(climberOuter, ClimberConfig.OUTER_ADVANCE_HIGH),
      new CommandFullTiltRetract(climberTilt).beforeStarting(new WaitCommand(1))
    );
    SmartDashboard.putData("Advance to high rung", advanceHighRungCommandGroup);

    //dpadRight.whileActiveContinuous(commandStartTilt);
    //dpadLeft.whileActiveContinuous(commandRetractTilt);
   // leftStickUp.whenActive(commandFullTilt);
   // leftStickDown.whenActive(commandFullTiltRetract);

    // rightStickDown.whenActive(commandRetractOuterArms)
    //       .whenInactive(commandStopOuterArms);
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