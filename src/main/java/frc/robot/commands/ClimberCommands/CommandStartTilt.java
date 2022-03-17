package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Climber;

public class CommandStartTilt extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
        private Climber climber;
    
    // **********************************************
    // Constructors
    // **********************************************

        public CommandStartTilt(Climber climber){
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
        // TODO Auto-generated method stub
        super.end(interrupted);
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        climber.stopTilt();
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climber.startTilt("forward");
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
