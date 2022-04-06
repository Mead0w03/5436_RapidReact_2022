package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;
import frc.robot.Constants.ClimberConfig;
import frc.robot.utils.PIDCoef;

public class Climber extends SubsystemBase {
// **********************************************
// Class Variables
// **********************************************


// **********************************************
// Instance Variables
// **********************************************
// Actuators
private TalonFX innerArmMotor;
private VictorSPX solenoidMotor;




// arm speed variables
private double innerArmSpeed = 0.6;

private final double startSpeed = 0.5;

private boolean solenoidEngaged = false;

private boolean ignoreEncoder = true;
private boolean resetInnerArmEncoder = false;




private final String subsystemName = "Climber";

private NetworkTable netTblClimber = NetworkTableInstance.getDefault().getTable(subsystemName);

private NetworkTableEntry entryCurrentCommand= netTblClimber.getEntry("Climber Command");

// Arm Speed Set Points
private NetworkTableEntry entryInnerArmSpeed= netTblClimber.getEntry("Inner Arm Speed");


// Actual Motor Speeds
private NetworkTableEntry entryInnerArmMotorSpeed= netTblClimber.getEntry("InnerArmMotorSpeed");


// Encoder Resets
private NetworkTableEntry entryResetInnerArmEncoder = netTblClimber.getEntry("Reset Inner Arm Encoder");


// Encoder override
private NetworkTableEntry entryIgnoreEncoder = netTblClimber.getEntry("Ignore Encoder");

// Encoder Setpoints


// Solenoid State
private NetworkTableEntry entrySolenoidEngaged = netTblClimber.getEntry("SolenoidEngaged");

// Encoder Values

private NetworkTableEntry entryInnerArmPos = netTblClimber.getEntry("Inner Encoder");


// Tilt PIDF Values
// private NetworkTableEntry entryTiltKp = netTblClimber.getEntry("Tilt kP");
// private NetworkTableEntry entryTiltKi = netTblClimber.getEntry("Tilt kI");
// private NetworkTableEntry entryTiltKd = netTblClimber.getEntry("Tilt kD");
// private NetworkTableEntry entryTiltKf = netTblClimber.getEntry("Tilt kF");

// Inner P Value
// private NetworkTableEntry entryInnerArmKp = netTblClimber.getEntry("Inner Arm kP");

// Outer P Value
// private NetworkTableEntry entryOuterArmKp = netTblClimber.getEntry("Outer Arm kP");

// private NetworkTableEntry entryTiltPidKp = netTblClimber.getEntry("Tilt PID kP");


// **********************************************
// Constructors
// **********************************************

public Climber(){
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    

    init();
    setNetworkTableListeners();

    // PID Coefficients
    
    PIDCoef innerArmCoef = new PIDCoef(subsystemName, "Inner Arm", innerArmMotor, 0.15);
   
    
    SmartDashboard.putData(this);   
}

// **********************************************
// Getters & Setters
// **********************************************


    public boolean getIgnoreEncoder(){
        return this.ignoreEncoder;
    }

   





    public double getClimberPosition(){
        return innerArmMotor.getSelectedSensorPosition();
    }



// **********************************************
// Class Methods
// **********************************************


// **********************************************
// Instance Methods
// **********************************************


    public void resetSpeed(){
        innerArmSpeed = startSpeed;
    }

    public void innerArmUp(){
        // todo:make speed variable
        innerArmMotor.set(ControlMode.PercentOutput, innerArmSpeed);
        // rightMotor.set(climbSpeed);
    }

    public void innerArmDown(){
        innerArmMotor.set(ControlMode.PercentOutput, -innerArmSpeed);
        // rightMotor.set(-climbSpeed);
    }

    public void stopAndEngageRatchet(){
        innerArmMotor.set(ControlMode.PercentOutput, 0.0);
        engageRatchet();
        // rightMotor.set(0);
    }

    public void stop(){
        innerArmMotor.set(ControlMode.PercentOutput, 0.0);
    }





   

    public void innerArmToPosition(double targetPosition){
        innerArmMotor.set(TalonFXControlMode.Position, targetPosition);
    } 
    


    



    public void engageRatchet(){
        solenoidMotor.set(ControlMode.PercentOutput, 0);
        solenoidEngaged = true;
    }

    public void releaseRatchet(){
        solenoidMotor.set(ControlMode.PercentOutput, 100);
        solenoidEngaged = false;
    }

    public void resetEncoder(){
        innerArmMotor.setSelectedSensorPosition(0.0);
    }


    @Override
    public void periodic() {
        // System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        // update current command
        entryCurrentCommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
        
        // Update actual motor speeds
        entryInnerArmMotorSpeed.setDouble(innerArmMotor.getMotorOutputPercent());
        
        // Update tiltRetractLimit switch state
        

        // update state of the solenoid
        entrySolenoidEngaged.setBoolean(solenoidEngaged);
        
        // update encoder  positions
        
        entryInnerArmPos.setDouble(innerArmMotor.getSelectedSensorPosition());
        

        entryInnerArmSpeed.setDouble(innerArmSpeed);
        
        
        

    }

    public void init() {
        // instantiate motors
        innerArmMotor = new TalonFX(CanBusConfig.INNER_ARM);
        solenoidMotor = new VictorSPX(CanBusConfig.SOLENOID);
        

        
        //instantiate sensors
        
        
    
        // clear all faults
        innerArmMotor.clearStickyFaults();
       
        solenoidMotor.clearStickyFaults();
        
    
        // set factory default for all motors
        innerArmMotor.configFactoryDefault();
        
        solenoidMotor.configFactoryDefault();
       
        
        // set braking mode for all
        innerArmMotor.setNeutralMode(NeutralMode.Brake);
        
        solenoidMotor.setNeutralMode(NeutralMode.Brake);
        
    
        // Invert motors
        innerArmMotor.setInverted(true);
        
    
        // Setup PID controller for tilt Motor
 

        // Setup PID controller for Inner Arm
        /* Config the sensor used for Primary PID and sensor direction */
        int timeOut = 30;
        int pidIndex = 0;
        int allowableError = 0;
        double maxPower = 0.8;
        innerArmMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, pidIndex, timeOut);
        innerArmMotor.configNominalOutputForward(0, timeOut);
		innerArmMotor.configNominalOutputReverse(0, timeOut);
		innerArmMotor.configPeakOutputForward(maxPower, timeOut);
		innerArmMotor.configPeakOutputReverse(-maxPower, timeOut);
        innerArmMotor.configAllowableClosedloopError(pidIndex, allowableError, timeOut);

		innerArmMotor.config_kF(pidIndex, 0, timeOut);
		innerArmMotor.config_kP(pidIndex, 0.15, timeOut);
		innerArmMotor.config_kI(pidIndex, 0, timeOut);
		innerArmMotor.config_kD(pidIndex, 0, timeOut);


        // Setup PID controller for Outer Arm
        

		/* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
		


        // engage the ratchet
        engageRatchet();

        // zero the endoers
        innerArmMotor.setSelectedSensorPosition(0.0);
        
        

    }

    //bind to button if needed
    /*
    public void stopAtLimit() {
        
        if(!climberLimit.get()) {
            stop();
        } else {
            //do nothing
        }
    } */

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void simulationPeriodic() {
        super.simulationPeriodic();
    }
    

    @Override
    public void initSendable(SendableBuilder builder) {
        // TODO Auto-generated method stub
        System.out.println("Running initSendable in Climber");
        builder.addDoubleProperty("Inner Arm Speed", () -> this.innerArmSpeed, (value) -> {this.innerArmSpeed = value; System.out.printf("innerArmSpeed=%.2f\n", innerArmSpeed);});
        
        
        builder.addDoubleProperty("Enter Inner Climb Mode", () -> ClimberConfig.INNER_ENTER_CLIMB, (value) -> ClimberConfig.INNER_ENTER_CLIMB = value);
        builder.addDoubleProperty("Inner Prep Mid Climb", () -> ClimberConfig.INNER_PREP_MID, (value) -> ClimberConfig.INNER_PREP_MID = value);
        builder.addDoubleProperty("Inner Mid Climb", () -> ClimberConfig.INNER_CLIMB_MID, (value) -> ClimberConfig.INNER_CLIMB_MID = value);
        builder.addDoubleProperty("Inner Advance High", () -> ClimberConfig.INNER_ADVANCE_HIGH, (value) -> ClimberConfig.INNER_ADVANCE_HIGH = value);
        builder.addDoubleProperty("Outer Enter Climb", () -> ClimberConfig.OUTER_ENTER_CLIMB, (value) -> ClimberConfig.OUTER_ENTER_CLIMB = value);
        builder.addDoubleProperty("Outer Reach Mid", () -> ClimberConfig.OUTER_REACH_MID, (value) -> ClimberConfig.OUTER_REACH_MID = value);
        builder.addDoubleProperty("Outer Advance High", () -> ClimberConfig.OUTER_ADVANCE_HIGH, (value) -> ClimberConfig.OUTER_ADVANCE_HIGH = value);
        builder.addDoubleProperty("Full Tilt Out", () -> ClimberConfig.FULLY_TILTED_OUT, (value) -> ClimberConfig.FULLY_TILTED_OUT = value);
        builder.addDoubleProperty("Full Tilt In", () -> ClimberConfig.FULLY_TILTED_IN, (value) -> ClimberConfig.FULLY_TILTED_IN = value);
    }

    public void setNetworkTableListeners(){
        
        // Create listeners for arm speeds and set initial values
        // *****************************************************************************************
        // // Inner Arm
        // entryInnerArmSpeed.setDouble(innerArmSpeed);
        // String innerArmEntryName = NetworkTable.basenameKey(entryInnerArmSpeed.getName());
        // netTblClimber.addEntryListener(innerArmEntryName, (table, key, entry, value, flags)->{
        //     System.out.println("The value for innerArmSpeed changed");
        //     if (innerArmSpeed != value.getDouble()){
        //         System.out.println("Updating inner arm speed from network table");
        //         innerArmSpeed = value.getDouble();
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // // Outer Arm
        // entryOuterArmSpeed.setDouble(outerArmSpeed);
        // String outerArmEntryName = NetworkTable.basenameKey(entryOuterArmSpeed.getName());
        // netTblClimber.addEntryListener(outerArmEntryName, (table, key, entry, value, flags)-> {
        //     System.out.println("The value for outerArmSpeed changed");
        //     if (outerArmSpeed != value.getDouble()){
        //         System.out.println("Updating the instance att based on table data");
        //         outerArmSpeed = value.getDouble();
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // // Tilt Motoer
        // entryTiltSpeed.setDouble(tiltSpeed);
        // String tiltEntryName = NetworkTable.basenameKey(entryTiltSpeed.getName());
        // netTblClimber.addEntryListener(tiltEntryName, (table, key, entry, value, flags)-> {
        //     System.out.println("The value for tiltSpeed changed");
        //     if (tiltSpeed != value.getDouble()){
        //         System.out.println("Updating the instance att based on table data");
        //         tiltSpeed = value.getDouble();
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        // *****************************************************************************************



        // Encoder Resets
        // *****************************************************************************************
        // Inner Arm
        entryResetInnerArmEncoder.setBoolean(resetInnerArmEncoder);
        String resetInnerEncoderEntryName = NetworkTable.basenameKey(entryResetInnerArmEncoder.getName());
        netTblClimber.addEntryListener(resetInnerEncoderEntryName, (table, key, entry, value, flags)->{
            System.out.println("Inner Arm Encoder being reset");
            if (value.getBoolean()){
                System.out.println("Resetting inner arm encoder");
                innerArmMotor.set(TalonFXControlMode.PercentOutput, 0);
                innerArmMotor.setSelectedSensorPosition(0);
                resetInnerArmEncoder = false;
                entryResetInnerArmEncoder.setBoolean(resetInnerArmEncoder);
            }
        },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        // Outer Arm
      
        // *****************************************************************************************
        
        

        // Encoder override
        // *****************************************************************************************
        // entryIgnoreEncoder.setBoolean(ignoreEncoder);
        // String ignoreEncoderEntryName = NetworkTable.basenameKey(entryIgnoreEncoder.getName());
        // netTblClimber.addEntryListener(ignoreEncoderEntryName, (table, key, entry, value, flags)->{
        //     if (value.getBoolean()!=ignoreEncoder){
        //         System.out.println("Changing Ignore Encoders");    
        //         ignoreEncoder = value.getBoolean();
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        // *****************************************************************************************


        // // Tilt Time Limit
        // // *****************************************************************************************
        // entryTiltTimeLimit.setDouble(tiltTimeLimit);
        // String tiltTimeLimitEntryName = NetworkTable.basenameKey(entryTiltTimeLimit.getName());
        // netTblClimber.addEntryListener(tiltTimeLimitEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue > 0;
        //     boolean isWithinUpperBound = proposedValue < 5.0;
        //     boolean isNewValue = proposedValue != tiltTimeLimit;
        //     if (isNewValue && isPositive && isWithinUpperBound){
        //         System.out.println("Changing Tilt Time Limit");    
        //         tiltTimeLimit = proposedValue;
        //     } else {
        //         // set the entry back
        //         entryTiltTimeLimit.setDouble(tiltTimeLimit);
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        // *****************************************************************************************

        // Tilt PID Limits
        // *****************************************************************************************
        // kP
        // entryTiltKp.setDouble(tiltPIDController.getP());
        // String tiltKpEntryName = NetworkTable.basenameKey(entryTiltKp.getName());
        // netTblClimber.addEntryListener(tiltKpEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue >= 0;
        //     boolean isWithinUpperBound = proposedValue < 1.0;
        //     boolean isNewValue = proposedValue != tiltPIDController.getP();
        //     if (isNewValue && isPositive && isWithinUpperBound){
        //         System.out.println("Changing Tilt kP");    
        //         tiltPIDController.setP(proposedValue);
        //     } else {
        //         // set the entry back
        //         entryTiltKp.setDouble(tiltPIDController.getP());
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);


        // kI
        // entryTiltKi.setDouble(tiltPIDController.getI());
        // String tiltKiEntryName = NetworkTable.basenameKey(entryTiltKi.getName());
        // netTblClimber.addEntryListener(tiltKiEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue >= 0;
        //     boolean isWithinUpperBound = proposedValue < 1.0;
        //     boolean isNewValue = proposedValue != tiltPIDController.getI();
        //     if (isNewValue && isPositive && isWithinUpperBound){
        //         System.out.println("Changing Tilt kI");    
        //         tiltPIDController.setI(proposedValue);
        //     } else {
        //         // set the entry back
        //         entryTiltKi.setDouble(tiltPIDController.getI());
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // kD
        // entryTiltKd.setDouble(tiltPIDController.getD());
        // String tiltKdEntryName = NetworkTable.basenameKey(entryTiltKd.getName());
        // netTblClimber.addEntryListener(tiltKdEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue >= 0;
        //     boolean isWithinUpperBound = proposedValue < 1.0;
        //     boolean isNewValue = proposedValue != tiltPIDController.getD();
        //     if (isNewValue && isPositive && isWithinUpperBound){
        //         System.out.println("Changing Tilt kD");    
        //         tiltPIDController.setD(proposedValue);
        //     } else {
        //         // set the entry back
        //         entryTiltKd.setDouble(tiltPIDController.getD());
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // kF
        // entryTiltKf.setDouble(tiltPIDController.getFF());
        // String tiltKfEntryName = NetworkTable.basenameKey(entryTiltKf.getName());
        // netTblClimber.addEntryListener(tiltKfEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue >= 0;
        //     boolean isWithinUpperBound = proposedValue < 1.0;
        //     boolean isNewValue = proposedValue != tiltPIDController.getFF();
        //     if (isNewValue && isPositive && isWithinUpperBound){
        //         System.out.println("Changing Tilt kF");    
        //         tiltPIDController.setFF(proposedValue);
        //     } else {
        //         // set the entry back
        //         entryTiltKd.setDouble(tiltPIDController.getFF());
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        // *****************************************************************************************



        // Inner Arm PID Coefficients
        // *****************************************************************************************
        // kP
        // entryInnerArmKp.setDouble(0.15);
        // String innerArmKpEntryName = NetworkTable.basenameKey(entryInnerArmKp.getName());
        // netTblClimber.addEntryListener(innerArmKpEntryName, (table, key, entry, value, flags)->{
        //     double proposedValue = value.getDouble();
        //     boolean isPositive = proposedValue >= 0;
        //     boolean isWithinUpperBound = proposedValue < 1.0;
        //     if (isPositive && isWithinUpperBound){
        //         System.out.println("Changing Inner Arm kP");    
        //         innerArmMotor.config_kP(0, proposedValue);
        //     } else {
        //         // set the entry back
        //         // entryInnerArmKp.setDouble(tiltPIDController.getP());
        //     }
        // },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);


        // Outer Arm PID Coefficients
        // *****************************************************************************************
        // kP
        // entryOuterArmKp.setDouble(0.15);
        //  String outerArmKpEntryName = NetworkTable.basenameKey(entryOuterArmKp.getName());
        //  netTblClimber.addEntryListener(outerArmKpEntryName, (table, key, entry, value, flags)->{
        //      double proposedValue = value.getDouble();
        //      boolean isPositive = proposedValue >= 0;
        //      boolean isWithinUpperBound = proposedValue < 1.0;
        //      if (isPositive && isWithinUpperBound){
        //          System.out.println("Changing Outer Arm kP");    
        //          outerArmMotor.config_kP(0, proposedValue);
        //      } else {
        //          // set the entry back
        //          // entryInnerArmKp.setDouble(tiltPIDController.getP());
        //      }
        //  },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);


    }

    
    
}
