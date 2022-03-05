// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class CommandFullTiltRetract extends CommandBase {
  /** Creates a new CommandFullTiltRetract. */
  private Climber climber;
  public CommandFullTiltRetract(Climber climber) {
    this.addRequirements(climber);
    this.climber = climber;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.stopTilt();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return climber.getTiltRetractLimit();
  }
}
