package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class CommandRetractTilt extends CommandBase {
    private Climber climber;
    
    
    // **********************************************
    // Constructors
    // **********************************************

        public CommandRetractTilt(Climber climber){
            System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            //this.addRequirements(climber);
            this.climber = climber;
        }
    
    // **********************************************
    // Getters & Setters
    // **********************************************
    
    
    // **********************************************
    // Class Methods
    // **********************************************
    
    
    // **********************************************
    // Instance Methods
    // **********************************************
    
    
    @Override
    public void end(boolean interrupted) {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        climber.stopTilt();
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        climber.startTilt("retract");
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
    }

    @Override
    public boolean isFinished() {
        // boolean encoderActive = !climber.getIgnoreEncoder();
        // boolean isFullyRetracted = climber.getIsTiltFullyRetracted();
        // return (encoderActive && isFullyRetracted);
        return false;
    }
    
}
