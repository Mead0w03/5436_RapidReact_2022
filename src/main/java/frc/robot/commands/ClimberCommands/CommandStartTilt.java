package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.ClimberTilt;

public class CommandStartTilt extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
        private ClimberTilt climberTilt;
    
    // **********************************************
    // Constructors
    // **********************************************

        public CommandStartTilt(ClimberTilt climberTilt){
            System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            //this.addRequirements(climber);
            this.climberTilt = climberTilt;
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
        climberTilt.stopTilt();
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climberTilt.startTilt("forward");
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public boolean isFinished() {
        boolean encoderActive = !climberTilt.getIgnoreEncoder();
        boolean isFullyTilted = climberTilt.getIsFullyTiltedOut();
        return (encoderActive && isFullyTilted);
        // return false;
    }
    
}
