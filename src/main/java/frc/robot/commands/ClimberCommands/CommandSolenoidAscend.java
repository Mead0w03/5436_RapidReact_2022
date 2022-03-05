//stop solenoid, ascend, engage solenoid then descend
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj.Timer;

public class CommandSolenoidAscend extends CommandBase {

  private Climber climber;
  private Timer timer;
  private double targetEncoderPosition;
  private double targetMovement = Constants.ClimberConfig.TARGET_MOVEMENT;

  /** Creates a new CommandStopSolenoid. */
  public CommandSolenoidAscend(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.addRequirements(climber);
    this.climber = climber;
    timer = new Timer();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
      timer.reset();
      timer.start();
      targetEncoderPosition = climber.getClimberPosition() + targetMovement;
      System.out.println(String.format("The current climber position is %.2f", climber.getClimberPosition()));   
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.innerArmUp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    timer.stop();
    climber.stop();
    System.out.println(String.format("The current climber position is %.2f", climber.getClimberPosition()));    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (climber.getClimberPosition() > targetEncoderPosition) || timer.get() > 1.0 ? true : false;
  }
}