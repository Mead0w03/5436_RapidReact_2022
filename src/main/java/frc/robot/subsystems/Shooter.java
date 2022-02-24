package frc.robot.subsystems;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;


public class Shooter extends SubsystemBase {

// **********************************************
// Class Variables
// **********************************************

// **********************************************
// Instance Variables
// **********************************************


private double kP = 0.0;
private double kI = 0.0;
private double targetVelocity_UnitsPer100ms = Speed.FAR_HIGH_GOAL.getSpeed();
private double feederSpeed = Speed.FEEDER.getSpeed();
private enum Speed{
    FAR_HIGH_GOAL (14000),
    CLOSE_LOW_GOAL (6000),
    FEEDER (3000);
    //close high has not been created but drivers say it is a position
    //CLOSE_HIGH_GOAL (#),

    //STOP_SHOOTER (0.0);

    private double speed;
    Speed (double speedIn){
        this.speed = speedIn;
    
    }
    public double getSpeed(){
        return this.speed;
    }
}


//make sure we have the right motors for falcon
private TalonFX leftShooterMotor;
private TalonFX rightShooterMotor;
private int loops;


public NetworkTable shooterTable = NetworkTableInstance.getDefault().getTable(this.getClass().getSimpleName()); // shooter speed
public NetworkTableEntry entryShooterCurrentcommand = shooterTable.getEntry("Shooter current command");
public NetworkTableEntry entryShooterPercentage = shooterTable.getEntry("Shooter percentage"); //shooter speed in % form
public NetworkTableEntry entryShooterSpeed = shooterTable.getEntry("Shooter speed"); //shooter speed
public NetworkTableEntry entryLeftShooterVelocity = shooterTable.getEntry("Left Shooter Velocity"); //shooter speed
public NetworkTableEntry entryRightShooterVelocity = shooterTable.getEntry("Right Shooter Velocity"); //shooter speed
public NetworkTableEntry entryAverageShooterVelocity = shooterTable.getEntry("Average Shooter Velocity"); //shooter velocity
public NetworkTableEntry entryLeftMotorSupplyCurrent = shooterTable.getEntry("Left Motor Supply Current Draw"); //shooter velocity
public NetworkTableEntry entryLeftMotorStatorCurrent = shooterTable.getEntry("Left Motor Stator Current Draw"); //shooter velocity
public NetworkTableEntry entryLeftMotorVelocityError = shooterTable.getEntry("Left Motor Velocity Error"); //shooter velocity
public NetworkTableEntry entryLeftMotorTargetVelocity = shooterTable.getEntry("Left Motor Target Velocity (clicks/100ms)"); //shooter velocity
public NetworkTableEntry entryTargetVelocity = shooterTable.getEntry("Target Velocity"); //shooter velocity
public NetworkTableEntry entryF = shooterTable.getEntry("F coeficient"); //shooter velocity
public NetworkTableEntry entryP = shooterTable.getEntry("P coeficient"); //shooter velocity
public NetworkTableEntry entryI = shooterTable.getEntry("I coeficient"); //shooter velocity
public NetworkTableEntry entryD = shooterTable.getEntry("D coeficient"); //shooter velocity
 
// **********************************************
// Constructors
// **********************************************




public Shooter(){
    shooterTable.addEntryListener("Target Velocity", (table, key, entry, value, flags) ->{
        System.out.println("shooter target speed changed value: " + value.getValue());
        targetVelocity_UnitsPer100ms = entryTargetVelocity.getDouble(0);
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    leftShooterMotor = new TalonFX(Constants.CanBusConfig.LAUNCHER);
    leftShooterMotor.setInverted(false);
    leftShooterMotor.configClosedloopRamp(0.5);
    leftShooterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    leftShooterMotor.setSensorPhase(true);
    leftShooterMotor.configNominalOutputForward(0, 30);
    leftShooterMotor.configPeakOutputForward(1, 30);
    leftShooterMotor.configNominalOutputReverse(0, 30);
    leftShooterMotor.configPeakOutputReverse(-1, 30);

    leftShooterMotor.config_kP(0, kP, 30);
    leftShooterMotor.config_kI(0, kI, 30);
    leftShooterMotor.config_kD(0, 0, 30);
    leftShooterMotor.config_kF(0, 0.05, 30);

    rightShooterMotor = new TalonFX(Constants.CanBusConfig.FEEDER);
    rightShooterMotor.setInverted(true);
    rightShooterMotor.configClosedloopRamp(0.5);
    rightShooterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    rightShooterMotor.setSensorPhase(true);
    rightShooterMotor.configNominalOutputForward(0, 30);
    rightShooterMotor.configPeakOutputForward(1, 30);
    rightShooterMotor.configNominalOutputReverse(0, 30);
    rightShooterMotor.configPeakOutputReverse(-1, 30);

    rightShooterMotor.config_kP(0, kP, 30);
    rightShooterMotor.config_kI(0, kI, 30);
    rightShooterMotor.config_kD(0, 0, 30);
    rightShooterMotor.config_kF(0, 0.05, 30);

    //add an entry listener for changed values of "X", the lambda ("->" operator)
    //defines the code that should run when "X" changes
    shooterTable.addEntryListener("P coeficient", (table, key, entry, value, flags) -> {
        kP = value.getDouble();
        System.out.println(String.format("p changed value: %.2f", kP));
        leftShooterMotor.config_kP(0, kP, 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    shooterTable.addEntryListener("I coeficient", (table, key, entry, value, flags) -> {
        kI = value.getDouble();
        System.out.println(String.format("I changed value: %.2f", kP));
        leftShooterMotor.config_kI(0, kI, 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

}
public void SpeedEnum(){

}


// **********************************************
// Instance Methods
// **********************************************

    public void activateShooter(){    
        leftShooterMotor.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
    }

    public void farHighGoal(){
        targetVelocity_UnitsPer100ms = Speed.FAR_HIGH_GOAL.getSpeed();
    }

    public void closeLowGoal(){
        targetVelocity_UnitsPer100ms = Speed.CLOSE_LOW_GOAL.getSpeed();
    }
    //close high has not been created but drivers say it is a position
    // public void closeHighGoal(){
    //     targetVelocity_UnitsPer100ms = Speed.CLOSE_HIGH_GOAL.getSpeed();
    // }

    public void reverseShooter(){
        targetVelocity_UnitsPer100ms = -Speed.CLOSE_LOW_GOAL.getSpeed();    
        leftShooterMotor.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
        
    }

    public void startFeederMotor(){
        targetVelocity_UnitsPer100ms = Speed.CLOSE_LOW_GOAL.getSpeed();    
        rightShooterMotor.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
        
    }

    public void stopFeeder(){
        rightShooterMotor.set(ControlMode.Velocity, feederSpeed);
        
    }

    public void stopShooter(){
        leftShooterMotor.set(ControlMode.PercentOutput, 0.0); 
    }


    public void periodic() {
        //Get the current command running on the subsystem
        entryShooterCurrentcommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
        // should print out:
        entryShooterPercentage.setDouble(leftShooterMotor.getMotorOutputPercent()); //motor in percent power
        entryLeftShooterVelocity.setDouble(leftShooterMotor.getSelectedSensorVelocity()); //left shooter RPM
        entryLeftMotorSupplyCurrent.setDouble(leftShooterMotor.getSupplyCurrent());
        entryLeftMotorStatorCurrent.setDouble(leftShooterMotor.getStatorCurrent());
        entryLeftMotorVelocityError.setDouble(leftShooterMotor.getClosedLoopError(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
        entryTargetVelocity.setDouble(targetVelocity_UnitsPer100ms);
        entryP.setDouble(kP);
        entryI.setDouble(kI);
        // //without encoders
        // shooterPercentage = shooterSpeed * 100;
        // entryShooterPercentage.setDouble(shooterPercentage);

        StringBuilder sb = new StringBuilder();
        sb.append("\tout");
        sb.append("%");
        sb.append("\tspd");
        sb.append(leftShooterMotor.getSelectedSensorVelocity());
        sb.append("u");
        sb.append("\terr: ");
        sb.append(leftShooterMotor.getClosedLoopError(0));
        sb.append("\ttrg: ");
        sb.append(targetVelocity_UnitsPer100ms);
    

        if(++loops >= 10){
            loops = 0;
          //  System.out.println(sb.toString());
        }
        sb.setLength(0);
    }

    @Override
    public void register() {
        // TODO Auto-generated method stub
        super.register();
    }

    @Override
    public void simulationPeriodic() {
        entryShooterPercentage.setDouble(leftShooterMotor.getMotorOutputPercent()); //motor in percent power
        entryLeftShooterVelocity.setDouble(leftShooterMotor.getSelectedSensorVelocity()); //left shooter RPM
        entryLeftMotorSupplyCurrent.setDouble(leftShooterMotor.getSupplyCurrent());
        entryLeftMotorStatorCurrent.setDouble(leftShooterMotor.getStatorCurrent());
        entryLeftMotorVelocityError.setDouble(leftShooterMotor.getClosedLoopError(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
        entryTargetVelocity.setDouble(targetVelocity_UnitsPer100ms);
        entryP.setDouble(kP);
        entryI.setDouble(kI);
    }

} 
