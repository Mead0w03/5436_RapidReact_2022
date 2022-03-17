package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;

public class CommandRetractOuterArmsToLegalLimit extends CommandBase {
    private Climber climber;
    private Timer timer;
    private double timeLimit = 1.5;
    
    
    // **********************************************
    // Constructors
    // **********************************************

        public CommandRetractOuterArmsToLegalLimit(Climber climber){
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
        climber.stopOuterArms();
        
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climber.startOuterArms("retract");
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        timer.reset();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        boolean shouldExit;
        if (climber.getIgnoreEncoder()){
            // if ignoring the encoder values, this automated routine should exit
            shouldExit = true;
        } else {
            // encoders are active, render decision based on position of outer arms
            boolean isLengthOk = climber.getOuterClimberPosition() < Constants.ClimberConfig.OUTER_LEGAL_LIMIT;
            boolean isTimedOut = timer.get() > timeLimit;
            shouldExit = isLengthOk || isTimedOut;
        }
        return shouldExit;

    }
    
}
