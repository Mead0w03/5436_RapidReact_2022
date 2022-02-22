package frc.robot.commands.ClimberCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Climber;

public class CommandDecreaseClimberSpeed extends CommandBase {


    private Climber climber;


    public CommandDecreaseClimberSpeed(Climber climber){

        this.addRequirements(climber);
        this.climber = climber;
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        super.end(interrupted);

    }

    @Override
    public void execute() {
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        climber.decreaseSpeed();
    }

    @Override
    public void initialize(){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));

    }

    @Override
    public boolean isFinished(){
        return true;
    }
}