// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
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

public class ClimberTilt extends SubsystemBase {

  private CANSparkMax tiltMotor;
  private DigitalInput tiltRetractLimit;
  private RelativeEncoder tiltEncoder;

  
  private double tiltSpeed = 0.25;

  // Controllers
  private SparkMaxPIDController tiltPIDController;

  
  private boolean resetTiltEncoder = false;
  private boolean ignoreEncoder = true;
  private double tiltTimeLimit = 1.5;

  private final String subsystemName = "Tilt Climber";

  //Network Table
  private NetworkTable netTblTiltClimber = NetworkTableInstance.getDefault().getTable(subsystemName);

  private NetworkTableEntry entryTiltSpeed= netTblTiltClimber.getEntry("Tilt Speed");
  
  private NetworkTableEntry entryTiltMotorSpeed= netTblTiltClimber.getEntry("TiltMotorSpeed");
  
  private NetworkTableEntry entryResetTiltEncoder = netTblTiltClimber.getEntry("Reset Tilt Encoder");
  
  private NetworkTableEntry entryTiltTimeLimit = netTblTiltClimber.getEntry("Tilt Time Limit");
  private NetworkTableEntry entryTiltRetractLimit = netTblTiltClimber.getEntry("Tilt Retract Limit Switch");
  
  private NetworkTableEntry entryTiltMotorPos = netTblTiltClimber.getEntry("Tilt Encoder");
  /** Creates a new ClimberTilt. */
  public ClimberTilt() {
    setNetworkTableListeners();
    init();

    PIDCoef tiltCoef = new PIDCoef(subsystemName, "Tilt", tiltMotor, 0.2);
   

    SmartDashboard.putData(this);
  }

public boolean getIgnoreEncoder(){
  return this.ignoreEncoder;
}


public boolean getIsTiltFullyRetracted(){
    return tiltMotor.getEncoder().getPosition() <= ClimberConfig.FULLY_TILTED_IN;
}

public boolean getIsFullyTiltedOut(){
    return tiltMotor.getEncoder().getPosition() >= ClimberConfig.FULLY_TILTED_OUT;
}

public void startTilt(String direction){
    double speed = 0.0;

    if(direction.equals("forward")) {
        speed = tiltSpeed;            
    } else if(direction.equals("retract")) {
        speed = -tiltSpeed;
    }

    tiltMotor.set(speed);
  }
public void stopTilt(){
    tiltMotor.set(0.0);
  }


public NetworkTableEntry getEntryTiltTimeLimit() {
  return entryTiltTimeLimit;
}

public void setEntryTiltTimeLimit(NetworkTableEntry entryTiltTimeLimit) {
  this.entryTiltTimeLimit = entryTiltTimeLimit;
}
public double getTiltTimeLimit(){
  return tiltTimeLimit;
}


public void tiltToPosition(double targetPosition){
  //tiltPIDController.setReference(targetPosition, ControlType.kPosition)
  //setReference(targetPosition, ControlType.kPosition);
}

  public void init(){
    int timeOut = 30;
    double maxPower = 0.8;
    int allowableError = 0;
    int pidIndex = 0;
    tiltMotor = new CANSparkMax(CanBusConfig.TILT, MotorType.kBrushless);
    tiltEncoder = tiltMotor.getEncoder();
    tiltMotor.clearFaults();
    
    tiltMotor.restoreFactoryDefaults();
    
    tiltMotor.setIdleMode(IdleMode.kBrake);
    tiltMotor.setInverted(true);

    

    tiltEncoder.setPosition(0.0);
    tiltPIDController = tiltMotor.getPIDController();
    tiltPIDController.setP(0.2);
    tiltPIDController.setOutputRange(-0.3, 0.3);
    tiltRetractLimit = new DigitalInput(9);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
   
    entryTiltMotorSpeed.setDouble(tiltMotor.get());
    

    
    entryTiltSpeed.setDouble(tiltSpeed);
    entryTiltMotorPos.setDouble(tiltEncoder.getPosition());
    entryTiltRetractLimit.setBoolean(tiltRetractLimit.get());
    entryTiltTimeLimit.setDouble(tiltTimeLimit);
  }

  public void setNetworkTableListeners(){
    
    
    // Tilt Motor
    entryResetTiltEncoder.setBoolean(resetTiltEncoder);
    String resetTiltEncoderEntryName = NetworkTable.basenameKey(entryResetTiltEncoder.getName());
    netTblTiltClimber.addEntryListener(resetTiltEncoderEntryName, (table, key, entry, value, flags)->{
        if (value.getBoolean()){
            System.out.println("Resetting Tilt Encoder");
            tiltMotor.set(0);
            tiltEncoder.setPosition(0.0);
            resetTiltEncoder = false;
            entryResetTiltEncoder.setBoolean(resetTiltEncoder);
        }
    },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Tilt Speed", () -> this.tiltSpeed, (value) -> this.tiltSpeed = value);
        builder.addDoubleProperty("Tilt Time Limit", () -> this.tiltTimeLimit, (value) -> this.tiltTimeLimit = value);
        builder.addBooleanProperty("Ignore Encoders", () -> this.ignoreEncoder, (value) -> {this.ignoreEncoder = value; System.out.printf("Setting encoder %s\n", ignoreEncoder);});
  }
}
