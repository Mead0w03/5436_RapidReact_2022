package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class CommandIntakeDownManual extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
    private Intake intake;
    
    // **********************************************
    // Constructors
    // **********************************************

        public CommandIntakeDownManual(Intake intake){
            System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            this.addRequirements(intake);
            this.intake = intake;
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
        
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        intake.intakeMove("Down", "Manual");
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}