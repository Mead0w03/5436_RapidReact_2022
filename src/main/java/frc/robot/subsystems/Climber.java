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
private TalonFX innerArmMotor;
private TalonFX outerArmMotor;
private VictorSPX tiltMotor;
private VictorSPX solenoidMotor;

private final XboxController xboxController;
private final XboxController.Axis tiltAxis;
private final XboxController.Axis outerArmAxis;

private double climbSpeed = 0.5;
private double rateOfChange = .05;
private double tiltSpeed = 0.5;
private double outerArmSpeed = 0.5;
private final double startSpeed = 0.5;
private boolean solenoidEngaged = false;

private final String netTblName = "Climber";
private NetworkTable netTblClimber = NetworkTableInstance.getDefault().getTable(netTblName);

private NetworkTableEntry entryCurrentCommand= netTblClimber.getEntry("Climber Command");
private final String climbSpeedEntryName = "Climber Speed";

private NetworkTableEntry entryClimberSpeed= netTblClimber.getEntry(climbSpeedEntryName);
private NetworkTableEntry entryTiltSpeed= netTblClimber.getEntry("Tilt Speed");
private NetworkTableEntry entryOuterArmSpeed= netTblClimber.getEntry("Outer Arm Speed");


private NetworkTableEntry entryInnerArmMotorSpeed= netTblClimber.getEntry("InnerArmMotorSpeed");
private NetworkTableEntry entryOuterArmMotorSpeed= netTblClimber.getEntry("OuterArmMotorSpeed");
private NetworkTableEntry entryTiltMotorSpeed= netTblClimber.getEntry("TiltMotorSpeed");
private NetworkTableEntry entryInnerArmMotorCurrent= netTblClimber.getEntry("InnerArmMotorCurrent");
private NetworkTableEntry entryOuterArmMotorCurrent= netTblClimber.getEntry("OuterArmMotorCurrent");
private NetworkTableEntry entryTiltMotorCurrent= netTblClimber.getEntry("TiltMotorCurrent");
private NetworkTableEntry entrySolenoidMotorCurrent = netTblClimber.getEntry("SolenoidMotorCurrent");
private NetworkTableEntry entrySolenoidEngaged = netTblClimber.getEntry("SolenoidEngaged");


// **********************************************
// Constructors
// **********************************************

public Climber (XboxController xboxController, XboxController.Axis articulateAxis, XboxController.Axis advanceAxis){
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
    this.xboxController = xboxController;
    this.tiltAxis = articulateAxis;
    this.outerArmAxis = advanceAxis;

    innerArmMotor = new TalonFX(CanBusConfig.INNER_ARM);
    outerArmMotor = new TalonFX(CanBusConfig.OUTER_ARM);
    tiltMotor = new VictorSPX(CanBusConfig.TILT);
    solenoidMotor = new VictorSPX(CanBusConfig.SOLENOID);


    // set factory default for all motors
    innerArmMotor.configFactoryDefault();
    outerArmMotor.configFactoryDefault();
    tiltMotor.configFactoryDefault();
    solenoidMotor.configFactoryDefault();


    // set braking mode for all
    outerArmMotor.setNeutralMode(NeutralMode.Brake);
    innerArmMotor.setNeutralMode(NeutralMode.Brake);
    tiltMotor.setNeutralMode(NeutralMode.Brake);
    solenoidMotor.setNeutralMode(NeutralMode.Brake);

    startSolenoid();

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

    System.out.println(String.format("entryOuterArmSpeed.getName(): %s", entryOuterArmSpeed.getName()));
    String outerArmEntryName = NetworkTable.basenameKey(entryOuterArmSpeed.getName());
    netTblClimber.addEntryListener(outerArmEntryName, (table, key, entry, value, flags)-> {
        System.out.println("The value for climber speed changed");
        if (outerArmSpeed != value.getDouble()){
            System.out.println("Updating the instance att based on table data");
            outerArmSpeed = value.getDouble();
        }
    },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    
    System.out.println(String.format("entryTiltSpeed.getName(): %s", entryTiltSpeed.getName()));
    String tiltEntryName = NetworkTable.basenameKey(entryTiltSpeed.getName());
    netTblClimber.addEntryListener(tiltEntryName, (table, key, entry, value, flags)-> {
        System.out.println("The value for climber speed changed");
        if (tiltSpeed != value.getDouble()){
            System.out.println("Updating the instance att based on table data");
            tiltSpeed = value.getDouble();
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
        innerArmMotor.set(ControlMode.PercentOutput, climbSpeed);
        // rightMotor.set(climbSpeed);
    }

    public void descend(){
        innerArmMotor.set(ControlMode.PercentOutput, -climbSpeed);
        // rightMotor.set(-climbSpeed);
    }

    public void stop(){
        innerArmMotor.set(ControlMode.PercentOutput, 0.0);
        startSolenoid();
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

    public void startTilt(String direction){
        // limits speed between 0.1 - 0.5
        //tiltSpeed = xboxController.getRawAxis(tiltAxis.value) / 2.0;
        double speed = 0.0;

        if(direction.equals("forward")) {

            speed = tiltSpeed;            
        } else if(direction.equals("retract")) {
            speed = -tiltSpeed;
        }

        tiltMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopTilt(){
        //tiltSpeed = 0.0;
        tiltMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void startOuterArms(String direction){
        // limits speed between 0.1 - 0.5
        //outerArmSpeed = xboxController.getRawAxis(outerArmAxis.value) / 2.0;
        double speed = 0.0;

        if(direction.equalsIgnoreCase("forward")){
            speed = outerArmSpeed;
        } else if(direction.equalsIgnoreCase("retract")) {
           speed = -outerArmSpeed;
        }
        outerArmMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopAdvance(){
//outerArmSpeed = 0.0;
        outerArmMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void startSolenoid(){
        solenoidMotor.set(ControlMode.PercentOutput, 100);
        solenoidEngaged = true;
    }

    public void stopSolenoid(){
        solenoidMotor.set(ControlMode.PercentOutput, 0);
        solenoidEngaged = false;
    }

    @Override
    public void periodic() {
        // System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        entryCurrentCommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
        entryClimberSpeed.setDouble(climbSpeed);
        // entryRightMotorSpeed.setDouble(rightMotor.get());
        entryInnerArmMotorSpeed.setDouble(innerArmMotor.getMotorOutputPercent());
        entryOuterArmMotorSpeed.setDouble(outerArmMotor.getMotorOutputPercent());
        entryTiltMotorSpeed.setDouble(tiltMotor.getMotorOutputPercent());
        
        entryInnerArmMotorCurrent.setDouble(innerArmMotor.getSupplyCurrent());
        entryOuterArmMotorCurrent.setDouble(outerArmMotor.getSupplyCurrent());
        // entryTiltMotorCurrent.setDouble(tiltMotor.getSupplyCurrent());
        entryTiltSpeed.setDouble(tiltSpeed);
        entryOuterArmSpeed.setDouble(outerArmSpeed);

        entrySolenoidEngaged.setBoolean(solenoidEngaged);

        

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
