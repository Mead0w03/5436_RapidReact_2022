package frc.robot.commands.AutonCommands;

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;


public class AutonShooterCommand extends CommandBase {

private Shooter autonShooter;
private Timer timer;

public AutonShooterCommand(Shooter shooter){
    this.autonShooter = shooter;
    this.addRequirements(autonShooter);
    timer = new Timer();
    
    System.out.println("In AutonShooterCommand");
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
     System.out.println("Execute,AutonShooterCommand");
     //SmartDashboard.putString("Execute", "AutonShooterCommand");
 }

 // Called once the command ends or is interrupted.
 @Override
 public void end(boolean interrupted) {
    // SmartDashboard.putString("End" ,"AutonShooterCommand");
    autonShooter.stopShooter();
     System.out.println("End,AutonShooterCommand");
 }

 // Returns true when the command should end.
 @Override
 public boolean isFinished() {
    boolean shouldExit = false;
    if(timer.get() > 1.5){
        shouldExit = true;
    }
    return shouldExit;
}
}
