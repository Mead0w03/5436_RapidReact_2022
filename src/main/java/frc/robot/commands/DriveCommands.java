package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBase;

public class DriveCommands extends CommandBase{
    private DriveBase driveBase;
    private Joystick stick;


    public DriveCommands(DriveBase driveBase, Joystick joy) {

        this.addRequirements(driveBase);
        this.driveBase = driveBase;
        this.stick = joy;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {

        double xOutput = stick.getRawAxis(0);
        double yOutput = stick.getRawAxis(1);
        xOutput = Math.signum(xOutput) * Math.pow(xOutput, 2);
        yOutput = Math.signum(yOutput) * Math.pow(yOutput, 2);

        double L = driveBase.driveProcessing(driveBase.deaden(xOutput, .1), driveBase.deaden(yOutput, .1), "Left")[0];
        double R = driveBase.driveProcessing(driveBase.deaden(xOutput, .1), driveBase.deaden(yOutput, .1), "Right")[1];

        SmartDashboard.putNumber("Left: ", L);
        SmartDashboard.putNumber("Right: ", R);
        SmartDashboard.putNumber("Y axis: ", yOutput);
        SmartDashboard.putNumber("X axis: ", xOutput);

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
