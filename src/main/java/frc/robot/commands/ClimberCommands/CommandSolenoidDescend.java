package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;


public class CommandSolenoidDescend extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
        private Climber climber;
        private RobotContainer robotContainer;
        private double targetEncoderPosition;
        private double targetMovement = -Constants.ClimberConfig.TARGET_MOVEMENT;
        private Timer timer = new Timer();
    // **********************************************
    // Constructors
    // **********************************************

        public CommandSolenoidDescend(Climber climber, RobotContainer robotContainer){
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
        if(robotContainer.getDpadUp()){
            // climber.stop();
            robotContainer.setOkToDescend(true);            
        }else{
            climber.stopAndEngageRatchet();
            robotContainer.setOkToDescend(false);
        }

        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        climber.innerArmUp();
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
        targetEncoderPosition = climber.getClimberPosition() + targetMovement;
        timer.reset();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        return climber.getClimberPosition() < targetEncoderPosition || timer.get() > 1.0 ? true : false;
    }
    
}
