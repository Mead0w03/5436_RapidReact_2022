// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ClimberCommands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class CommandFullTilt extends CommandBase {
  /** Creates a new CommandFullTilt. */
  private Timer timer = new Timer();
  private Climber climber;
  private double timeLimit;
  public CommandFullTilt(Climber climber) {
    
    //this.addRequirements(climber);
    this.climber = climber;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    climber.initTiltPositionControl();
    //timeLimit = NetworkTableInstance.getDefault().getTable("Climber").getEntry("Tilt Time Limit").getDouble(0.5);
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //climber.startTilt("forward");
    climber.tiltOut();
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    System.out.println(String.format("Tilt timed out at %.2f", timer.get()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //climber.stopTilt();
    System.out.println(String.format("Tilt timed out at %.2f", timer.get()));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
