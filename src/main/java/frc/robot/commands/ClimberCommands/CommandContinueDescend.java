package frc.robot.commands.ClimberCommands;
import frc.robot.Constants.ClimberConfig;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;

public class CommandContinueDescend extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
        private Climber climber;
        private RobotContainer robotContainer;
    // **********************************************
    // Constructors
    // **********************************************

        public CommandContinueDescend(Climber climber, RobotContainer robotContainer){
            System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            this.addRequirements(climber);
            this.climber = climber;
            this.robotContainer = robotContainer;
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
        robotContainer.setOkToDescend(false);
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
    }

    @Override
    public void execute() {
        //System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climber.innerArmUp();
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
    }

    @Override
    public boolean isFinished() {
        boolean isFullyDescended = climber.getClimberPosition() < ClimberConfig.INNER_FULLY_DESCENDED;
        if(isFullyDescended) robotContainer.setIsFullyDescended(true);
        return isFullyDescended;
    }
    
}
