// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;

public class CommandClimbAscend extends CommandBase {

  private Climber climber;
  /** Creates a new CommandClimbAscend. */
  public CommandClimbAscend(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.addRequirements(climber);
    this.climber = climber;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.innerArmUp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean verdict = false;
    if(!climber.getIgnoreEncoder() && climber.getClimberPosition() > Constants.ClimberConfig.INNER_FULLY_DESCENDED){
        verdict = true;
    }
    return verdict;
  }
}
