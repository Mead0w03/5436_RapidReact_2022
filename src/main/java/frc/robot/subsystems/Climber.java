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


// arm speed variables
private double innerArmSpeed = 0.6;

private final double startSpeed = 0.5;


private boolean ignoreEncoder = true;
private boolean resetInnerArmEncoder = false;

// **********************************************
// Constructors
// **********************************************

public Climber(){
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
    init();

    // PID Coefficients
    PIDCoef innerArmCoef = new PIDCoef(this.getClass().getSimpleName(), "Inner Arm", innerArmMotor, 0.15);
   
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
        // rightMotor.set(0);
    }

    public void stop(){
        innerArmMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void innerArmToPosition(double targetPosition){
        innerArmMotor.set(TalonFXControlMode.Position, targetPosition);
    } 
    
    // public void engageRatchet(){
    //     solenoidMotor.set(ControlMode.PercentOutput, 0);
    //     solenoidEngaged = true;
    // }

    // public void releaseRatchet(){
    //     solenoidMotor.set(ControlMode.PercentOutput, 100);
    //     solenoidEngaged = false;
    // }

    @Override
    public void periodic() {
        
    }

    public void init() {
        // instantiate motors
        innerArmMotor = new TalonFX(CanBusConfig.INNER_ARM);
        //solenoidMotor = new VictorSPX(CanBusConfig.SOLENOID);
        
        // clear all faults
        innerArmMotor.clearStickyFaults();    
        //solenoidMotor.clearStickyFaults();
        
    
        // set factory default for all motors
        innerArmMotor.configFactoryDefault();
        //solenoidMotor.configFactoryDefault();
       
        
        // set braking mode for all
        innerArmMotor.setNeutralMode(NeutralMode.Brake);
        //solenoidMotor.setNeutralMode(NeutralMode.Brake);
        
    
        // Invert motors
        innerArmMotor.setInverted(false);
        
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


        // engage the ratchet

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
    
    private void resetInnerArmEncoder(boolean resetFlag){
        // Reset Tilt Encoder when the value on the SmartDashboard is changed to True
        System.out.println("Resetting Inner Arm Encoder");
        // stop the motor
        innerArmMotor.set(TalonFXControlMode.PercentOutput, 0);
        // reset the encoders
        innerArmMotor.setSelectedSensorPosition(0);
        // set the flag back to false
        resetInnerArmEncoder = false;
    }


    @Override
    public void initSendable(SendableBuilder builder) {
        // TODO Auto-generated method stub
        System.out.println("Running initSendable in Climber");
        builder.addDoubleProperty("Inner Arm Speed", () -> this.innerArmSpeed, value -> this.innerArmSpeed = value);
        builder.addBooleanProperty("Ignore Inner Arm Encoder", () -> ignoreEncoder, (value) -> {ignoreEncoder = value; System.out.printf("Setting innerArm ignoreEncoder %s\n", ignoreEncoder);});
        builder.addBooleanProperty("Reset Inner Arm Encoder", () -> resetInnerArmEncoder, this::resetInnerArmEncoder);
        builder.addDoubleProperty("Inner Arm Encoder", () -> innerArmMotor.getSelectedSensorPosition(), value -> innerArmMotor.setSelectedSensorPosition(value));
        builder.addStringProperty("Inner Arm Command", () -> (this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName(), null);
        
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
  
    
}
