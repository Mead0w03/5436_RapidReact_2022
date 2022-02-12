package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;

public class Climber extends SubsystemBase {
// **********************************************
// Class Variables
// **********************************************


// **********************************************
// Instance Variables
// **********************************************
private TalonFX vertMotor;
private TalonFX advanceMotor;
private VictorSPX articulateMotor;
private VictorSPX SolenoidMotor;

private final XboxController xboxController;
private final XboxController.Axis articulateAxis;
private final XboxController.Axis advanceAxis;

private double climbSpeed = 0.1;
private double rateOfChange = .05;
private double articulateSpeed = 0.0;
private double advanceSpeed = 0.0;
private final double startSpeed = 0.1;

private final String netTblName = "Climber";
private NetworkTable netTblClimber = NetworkTableInstance.getDefault().getTable(netTblName);

private NetworkTableEntry entryCurrentCommand= netTblClimber.getEntry("Climber Command");
private final String climbSpeedEntryName = "Climber Speed";

private NetworkTableEntry entryClimberSpeed= netTblClimber.getEntry(climbSpeedEntryName);
private NetworkTableEntry entryArticulateSpeed= netTblClimber.getEntry("Articulate Speed");
private NetworkTableEntry entryAdvanceSpeed= netTblClimber.getEntry("Advance Speed");


private NetworkTableEntry entryVertMotorSpeed= netTblClimber.getEntry("VertMotorSpeed");
private NetworkTableEntry entryAdvanceMotorSpeed= netTblClimber.getEntry("AdvanceMotorSpeed");
private NetworkTableEntry entryArticulateMotorSpeed= netTblClimber.getEntry("ArticulateMotorSpeed");
private NetworkTableEntry entryVertMotorCurrent= netTblClimber.getEntry("VertMotorCurrent");
private NetworkTableEntry entryAdvanceMotorCurrent= netTblClimber.getEntry("AdvanceMotorCurrent");
private NetworkTableEntry entryArticulateMotorCurrent= netTblClimber.getEntry("ArticulateMotorCurrent");


// **********************************************
// Constructors
// **********************************************

public Climber (XboxController xboxController, XboxController.Axis articulateAxis, XboxController.Axis advanceAxis){
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
    this.xboxController = xboxController;
    this.articulateAxis = articulateAxis;
    this.advanceAxis = advanceAxis;

    vertMotor = new TalonFX(CanBusConfig.VERT_ARM);
    advanceMotor = new TalonFX(CanBusConfig.ADVANCE_ARM);
    articulateMotor = new VictorSPX(CanBusConfig.ARTICULATOR);
    SolenoidMotor = new VictorSPX(CanBusConfig.SOLENOID);


    // set factory default for all motors
    vertMotor.configFactoryDefault();
    advanceMotor.configFactoryDefault();
    articulateMotor.configFactoryDefault();
    SolenoidMotor.configFactoryDefault();


    // set braking mode for all
    advanceMotor.setNeutralMode(NeutralMode.Brake);
    vertMotor.setNeutralMode(NeutralMode.Brake);
    articulateMotor.setNeutralMode(NeutralMode.Brake);
    SolenoidMotor.setNeutralMode(NeutralMode.Brake);

    /*
    // set Current Limits
    double supplyLimit = 10;
    double triggerThreshold = 40;
    double triggerThresholdTime = 0.5;

    SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, supplyLimit, triggerThreshold, triggerThresholdTime);
    vertMotor.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    advanceMotor.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
*/

    // leftEncoder = leftMotor.getEncoder();
    // rightEncoder = rightMotor.getEncoder();

    // Configure the network table entries
    System.out.println(String.format("entryClimberSpeed.getName(): %s", entryClimberSpeed.getName()));
    netTblClimber.addEntryListener(climbSpeedEntryName, (table, key, entry, value, flags)->{
        System.out.println("The value for climber speed changed");
        if (climbSpeed != value.getDouble()){
            System.out.println("Updating the instance att based on table data");
            climbSpeed = value.getDouble();
        }
    },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

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

    public void resetSpeed(){
        climbSpeed = startSpeed;
    }

    public void ascend(){
        // todo:make speed variable
        vertMotor.set(ControlMode.PercentOutput, climbSpeed);
        // rightMotor.set(climbSpeed);
    }

    public void descend(){
        vertMotor.set(ControlMode.PercentOutput, -climbSpeed);
        // rightMotor.set(-climbSpeed);
    }

    public void stop(){
        vertMotor.set(ControlMode.PercentOutput, 0.0);
        // rightMotor.set(0);
    }
    
    public void increaseSpeed(){
        double newSpeed = climbSpeed += rateOfChange;
        if(newSpeed > 1.0) {
            newSpeed = 1;
        } else if(newSpeed < 0){
            newSpeed = rateOfChange;
        }
        climbSpeed = newSpeed;
    }

    public void decreaseSpeed(){
        double newSpeed = climbSpeed -= rateOfChange;
        if(newSpeed <= 0.0){
            newSpeed = rateOfChange;
        } else if (newSpeed >1){
            newSpeed = 1;
        }
        climbSpeed = newSpeed;
    }

    public void articulate(){
        // limits speed between 0.1 - 0.5
        articulateSpeed = xboxController.getRawAxis(articulateAxis.value) / 2.0;
        articulateMotor.set(ControlMode.PercentOutput, articulateSpeed);
    }

    public void stopArticulate(){
        articulateSpeed = 0.0;
        articulateMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void advance(){
        // limits speed between 0.1 - 0.5
        advanceSpeed = xboxController.getRawAxis(advanceAxis.value) / 2.0;
        advanceMotor.set(ControlMode.PercentOutput, advanceSpeed);
    }

    public void stopAdvance(){
        advanceSpeed = 0.0;
        advanceMotor.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public void periodic() {
        // System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        entryCurrentCommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
        entryClimberSpeed.setDouble(climbSpeed);
        // entryRightMotorSpeed.setDouble(rightMotor.get());
        entryVertMotorSpeed.setDouble(vertMotor.getMotorOutputPercent());
        entryAdvanceMotorSpeed.setDouble(advanceMotor.getMotorOutputPercent());
        entryArticulateMotorSpeed.setDouble(articulateMotor.getMotorOutputPercent());
        
        entryVertMotorCurrent.setDouble(vertMotor.getSupplyCurrent());
        entryAdvanceMotorCurrent.setDouble(advanceMotor.getSupplyCurrent());
        // entryArticulateMotorCurrent.setDouble(articulateMotor.getSupplyCurrent());
        // entryArticulateSpeed.setDouble(articulateSpeed);
        entryAdvanceSpeed.setDouble(advanceSpeed);


    }

    @Override
    public void register() {
        // TODO Auto-generated method stub
        super.register();
    }

    @Override
    public void simulationPeriodic() {
        // TODO Auto-generated method stub
        super.simulationPeriodic();
    }
    
}
