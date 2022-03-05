package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;

public class CommandClimb extends CommandBase{
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

        public CommandClimb(Climber climber, RobotContainer robotContainer){
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
        robotContainer.setIsFullyDescended(false);
        climber.stop();
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climber.innerArmDown();
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
    }

    @Override
    public boolean isFinished() {
        boolean verdict = false;
        if(!climber.getIgnoreEncoder() && climber.getClimberPosition() > Constants.ClimberConfig.FULLY_ASCENDED){
            verdict = true;
        }
        return verdict;
    }
    
}
