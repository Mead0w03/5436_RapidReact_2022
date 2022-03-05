package frc.robot.commands.AutonCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBase;

public class AutonDriveCommand extends CommandBase {

private DriveBase autonDriveBase;
private Timer timer;
private double speed = 0.2;

public AutonDriveCommand(DriveBase driveBase){
    this.autonDriveBase = driveBase;
    timer = new Timer();
    this.addRequirements(autonDriveBase);
    System.out.println("In AutonDriveCommand");
}
 // Called when the command is initially scheduled.
 @Override
 public void initialize() {
     timer.reset();
     timer.start();
     autonDriveBase.init();
     System.out.println("Initialize, AutonDriveCommand");
     //SmartDashboard.putString("Initialise", "AutonDriveCommand");
 }

 // Called every time the scheduler runs while the command is scheduled.
 @Override
 public void execute() {
     autonDriveBase.drive(speed, speed);
     
     System.out.println("Execute,AutonDriveCommand");
     //SmartDashboard.putString("Execute", "AutonDriveCommand");
 }

 // Called once the command ends or is interrupted.
 @Override
 public void end(boolean interrupted) {
    // SmartDashboard.putString("End" ,"AutonDriveCommand");
     System.out.println("End,AutonDriveCommand");
     autonDriveBase.stopAllDrivetrainMotors();
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
