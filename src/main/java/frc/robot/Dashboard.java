package frc.robot;

public class Dashboard {
    public Dashboard() {
        
    }

    init() {
        //Climber
            //SmartDashboard.putNumber("Middle Climber Speed: ", 0.0);
            //SmartDashboard.putNumber("Outer Climber Speed: ", 0.0);
            //SmartDashboard.putNumber("Tilt Climber Speed: ", 0.0);
            SmartDashboard.putNumber("Climber Encoders: ", 0.0);
        //Shooter
            SmartDashboard.putNumber("RPM: ", 0.0);
            SmartDashboard.putNumber("Manual RPM: ", 0.0);
            SmartDashboard.putNumber("Feeder Speed: ", 0.0);
        //Drive Base
            SmartDashboard.putNumber("Deadband: ", 0.0);
        //Intake
            //SmartDashboard.putNumber("Intake Arm Speed: ", 0.0);
            //SmartDashboard.putNumber("Intake Storage Speed: ", 0.0);


    }

    public static Double getDeadband() {
        return SmartDashboard.getNumber("Deadband: ", 0.0);
    }

    public static Double getFeederSpeed() {
        return SmartDashboard.getNumber("Feeder Speed: ", 0.0);
    }

    public static Double getManualRPM() {
        return SmartDashboard.getNumber("Manual RPM: ", 0.0);
    }

    





}