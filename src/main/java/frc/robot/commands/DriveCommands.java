package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBase;

public class DriveCommands extends CommandBase{
    private DriveBase driveBase;
    private Joystick stick;
    private PS4Controller ps4Controller;


    public DriveCommands(DriveBase driveBase, PS4Controller ps4) {

        this.addRequirements(driveBase);
        this.driveBase = driveBase;
        this.ps4Controller = ps4;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {

        double xOutput = ps4Controller.getRightX() * 0.5;
        double yOutput = ps4Controller.getLeftY() * 0.5;

       
        double L = driveBase.driveProcessing(driveBase.deaden(xOutput, .1), driveBase.deaden(yOutput, .1), "Left")[0];
        double R = driveBase.driveProcessing(driveBase.deaden(xOutput, .1), driveBase.deaden(yOutput, .1), "Right")[1];

        SmartDashboard.putNumber("Left: ", L);
        SmartDashboard.putNumber("Right: ", R);

        driveBase.drive(L, R);
    }

    @Override
    public void end(boolean interrupted) {
        //driveBase.stopAllDrivetrainMotors();
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  
}
