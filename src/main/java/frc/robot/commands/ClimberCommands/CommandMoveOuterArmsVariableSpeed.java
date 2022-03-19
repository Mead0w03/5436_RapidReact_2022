package frc.robot.commands.ClimberCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;

public class CommandMoveOuterArmsVariableSpeed extends CommandBase {
    private Climber climber;
    private DoubleSupplier sitckInput;
    
    // **********************************************
    // Constructors
    // **********************************************

    public CommandMoveOuterArmsVariableSpeed(Climber climber, DoubleSupplier stickInput){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
        //this.addRequirements(climber);
        this.climber = climber;
        this.sitckInput = stickInput;
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
        climber.stopOuterArms();
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // condition the signal
        double input = -sitckInput.getAsDouble();  //reverse sign because up is negative
        double speed = Math.signum(input) * (input*input);  //square the input and preserve the sign
        // run the motor
        climber.runOuterArmsToSpeed(speed);
        System.out.printf("Running the outer climber to %.2f", speed);
    }

    @Override
    public void initialize() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        // Reset the start speed of climp
        // climber.resetSpeed();
    }

    @Override
    public boolean isFinished() {
        
       boolean isFullyRetracted = climber.getOuterClimberPosition() < Constants.ClimberConfig.OUTER_FULLY_RETRACTED;
       boolean encoderActive = !climber.getIgnoreEncoder();
        return isFullyRetracted && encoderActive;

    }
    
}
