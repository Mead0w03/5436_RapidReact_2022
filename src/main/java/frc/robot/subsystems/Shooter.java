package frc.robot.subsystems;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;


public class Shooter extends SubsystemBase {

// **********************************************
// Class Variables
// **********************************************

// **********************************************
// Instance Variables
// **********************************************


private double kP = 0.15;
private double kI = 0.001;
private double targetVelocity_UnitsPer100ms;
private enum Speed{
    FAR_HIGH_GOAL (-14000),
    CLOSE_LOW_GOAL (-6000),
    //close high has not been created but drivers say it is a position
    //CLOSE_HIGH_GOAL (#),
    STOP_SHOOTER (0);
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
private int loops;


public NetworkTable shooterTable = NetworkTableInstance.getDefault().getTable(this.getClass().getSimpleName()); // shooter speed
public NetworkTableEntry entryShooterPercentage = shooterTable.getEntry("Shooter percentage"); //shooter speed in % form
public NetworkTableEntry entryShooterSpeed = shooterTable.getEntry("Shooter speed"); //shooter speed
public NetworkTableEntry entryLeftShooterVelocity = shooterTable.getEntry("Left Shooter Velocity"); //shooter speed
public NetworkTableEntry entryAverageShooterVelocity = shooterTable.getEntry("Average Shooter Velocity"); //shooter velocity
public NetworkTableEntry entryLeftMotorSupplyCurrent = shooterTable.getEntry("Left Motor Supply Current Draw"); //shooter velocity
public NetworkTableEntry entryLeftMotorStatorCurrent = shooterTable.getEntry("Left Motor Stator Current Draw"); //shooter velocity
public NetworkTableEntry entryLeftMotorVelocityError = shooterTable.getEntry("Left Motor Velocity Error"); //shooter velocity
public NetworkTableEntry entryLeftMotorTargetVelocity = shooterTable.getEntry("Left Motor Target Velocity (clicks/100ms)"); //shooter velocity
public NetworkTableEntry entryF = shooterTable.getEntry("F coeficient"); //shooter velocity
public NetworkTableEntry entryP = shooterTable.getEntry("P coeficient"); //shooter velocity
public NetworkTableEntry entryI = shooterTable.getEntry("I coeficient"); //shooter velocity
public NetworkTableEntry entryD = shooterTable.getEntry("D coeficient"); //shooter velocity
 
// **********************************************
// Constructors
// **********************************************




public Shooter(){
    leftShooterMotor = new TalonFX(CanBusConfig.LEFT_SHOOTER);
    leftShooterMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    leftShooterMotor.setSensorPhase(true);
    leftShooterMotor.configNominalOutputForward(0, 30);
    leftShooterMotor.configPeakOutputForward(1, 30);
    leftShooterMotor.configNominalOutputReverse(0, 30);
    leftShooterMotor.configPeakOutputReverse(-1, 30);

    leftShooterMotor.config_kP(0, kP, 30);
    leftShooterMotor.config_kI(0, kI, 30);
    leftShooterMotor.config_kD(0, 0, 30);
    leftShooterMotor.config_kF(0, 0, 30);

    //add an entry listener for changed values of "X", the lambda ("->" operator)
    //defines the code that should run when "X" changes
    shooterTable.addEntryListener("P coeficient", (table, key, entry, value, flags) -> {
        kP = value.getDouble();
        System.out.println(String.format("p changed value: %.2f", kP));
        leftShooterMotor.config_kP(0, kP, 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    shooterTable.addEntryListener("I coeficient", (table, key, entry, value, flags) -> {
        kI = value.getDouble();
        System.out.println(String.format("p changed value: %.2f", kP));
        leftShooterMotor.config_kI(0, kI, 30);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

}

public void SpeedEnum(){


}


// **********************************************
// Instance Methods
// **********************************************

    public void activateShooter(){
        targetVelocity_UnitsPer100ms = Speed.FAR_HIGH_GOAL.getSpeed();    
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
    }

    public void stopShooter(){
        targetVelocity_UnitsPer100ms = Speed.STOP_SHOOTER.getSpeed();    
    }


    public void periodic() {
        // should print out:
        entryShooterPercentage.setDouble(leftShooterMotor.getMotorOutputPercent()); //motor in percent power
        entryLeftShooterVelocity.setDouble(leftShooterMotor.getSelectedSensorVelocity()); //left shooter RPM
        entryLeftMotorSupplyCurrent.setDouble(leftShooterMotor.getSupplyCurrent());
        entryLeftMotorStatorCurrent.setDouble(leftShooterMotor.getStatorCurrent());
        entryLeftMotorVelocityError.setDouble(leftShooterMotor.getClosedLoopError(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
        entryLeftMotorTargetVelocity.setDouble(leftShooterMotor.getClosedLoopTarget(0));
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

        leftShooterMotor.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);

        sb.append("\terr: ");
        sb.append(leftShooterMotor.getClosedLoopError(0));
        sb.append("\ttrg: ");
        sb.append(targetVelocity_UnitsPer100ms);
    

        if(++loops >= 10){
            loops = 0;
            System.out.println(sb.toString());
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
        entryP.setDouble(kP);
        entryI.setDouble(kI);
    }

} 
