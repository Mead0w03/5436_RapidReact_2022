//stop solenoid, ascend, engage solenoid then descend
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj.Timer;

public class CommandSolenoidAscend extends CommandBase {

  private Climber climber;
  private Timer timer;

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
      timer.reset();
      timer.start();
      climber.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.ascend();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
    climber.stop();
    double currentClimberPosition = climber.getClimberPosition();
    System.out.println(String.format("The current climber position is %.2f", currentClimberPosition));    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return climber.getClimberPosition() > 18500 ? true : false;
  }
}