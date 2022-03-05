// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutonCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class AutonIntakeUpCommand extends CommandBase {
   
  private Intake intake;
  private Timer timer;

  /** Creates a new AutonIntakeUpCommand. */
  public AutonIntakeUpCommand(Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            this.addRequirements(intake);
            this.intake = intake;
            timer = new Timer();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void end(boolean interrupted) {
      
      super.end(interrupted);
      System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
      
  }

  @Override
  public void execute() {
      System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
      
      intake.intakeUp();
  }


  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean shouldExit = false;
     //SmartDashboard.putNumber("Timer", timer.get());
     System.out.println(String.format("Timer %.2f", timer.get()));
    if(timer.get() > 1.0){
        shouldExit = true;
    }
   return shouldExit;
}
  
}
