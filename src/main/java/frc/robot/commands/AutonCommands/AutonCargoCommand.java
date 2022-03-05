package frc.robot.commands.AutonCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;

public class AutonCargoCommand extends CommandBase{
    // **********************************************
    // Class Variables
    // **********************************************
    
    
    // **********************************************
    // Instance Variables
    // **********************************************
    private Intake intake;
    private Timer timer;
    
    // **********************************************
    // Constructors
    // **********************************************

        public AutonCargoCommand(Intake intake){
            System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
            timer = new Timer();
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
        timer.stop();
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        intake.cargoIn();
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        timer.reset();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        return timer.get()>1.5 ? true : false; //1.5 IS PLACEHOLDER VALUE
    }
}