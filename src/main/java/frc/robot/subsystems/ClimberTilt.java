// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

  /** Creates a new ClimberTilt. */
  public ClimberTilt() {
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    init();

    PIDCoef tiltCoef = new PIDCoef(this.getClass().getSimpleName(), "Tilt", tiltMotor, 0.2);
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


public double getTiltTimeLimit(){
  return tiltTimeLimit;
}


public void tiltToPosition(double targetPosition){
  tiltPIDController.setReference(targetPosition, CANSparkMax.ControlType.kPosition);
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
    tiltMotor.setInverted(false);

    
    tiltEncoder.setPosition(0.0);
    tiltPIDController = tiltMotor.getPIDController();
    tiltPIDController.setP(0.2);
    tiltPIDController.setOutputRange(-0.3, 0.3);
    tiltRetractLimit = new DigitalInput(9);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  private void resetTiltEncoder(boolean resetFlag){
    // Reset Tilt Encoder when the value on the SmartDashboard is changed to True
    System.out.println("Resetting Tilt Encoder");
    // stop the motor
    tiltMotor.set(0);
    // reset the encoders
    tiltEncoder.setPosition(0.0);
    // set the flag back to false
    resetTiltEncoder = false;
  }

  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Tilt Speed", () -> this.tiltSpeed, (value) -> this.tiltSpeed = value);
    builder.addDoubleProperty("Tilt Time Limit", () -> this.tiltTimeLimit, (value) -> this.tiltTimeLimit = value);
    builder.addBooleanProperty("Ignore Encoders", () -> this.ignoreEncoder, (value) -> {this.ignoreEncoder = value; System.out.printf("Setting tiltMotor ignoreEncoder %s\n", ignoreEncoder);});
    builder.addBooleanProperty("Reset Tilt Encoder", () -> this.resetTiltEncoder, this::resetTiltEncoder);
    builder.addDoubleProperty("Tilt Encoder", () -> tiltEncoder.getPosition(), null);
    builder.addStringProperty("Tilt Command", () -> (this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName(), null);

  }
}
