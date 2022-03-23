package frc.robot.commands.AutonCommands;

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;


public class AutonStartShooterCommand extends CommandBase {

private Shooter autonShooter;
private Timer timer;
private double timeLimit = 0.0;

public AutonStartShooterCommand(Shooter shooter, double timeLimit){
    this.autonShooter = shooter;
    this.timeLimit = timeLimit;
    timer = new Timer();
    this.addRequirements(autonShooter);
}

// Called when the command is initially scheduled.
@Override
public void initialize() {
    timer.reset();
    timer.start();
}
// Called every time the scheduler runs while the command is scheduled.
 @Override
 public void execute() {
    autonShooter.farHighGoal();
    autonShooter.activateShooter();
    System.out.println("Execute, AutonStartCommand");
 }

 // Called once the command ends or is interrupted.
 @Override
 public void end(boolean interrupted) {}

 // Returns true when the command should end.
 @Override
 public boolean isFinished() {
   
    return timer.get() > timeLimit ? true :false;
}
}
