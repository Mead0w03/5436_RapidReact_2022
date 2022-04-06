// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;
import frc.robot.Constants.ClimberConfig;
import frc.robot.utils.PIDCoef;

public class ClimberOuter extends SubsystemBase {
  /** Creates a new ClimberOuter. */
  private ClimberTilt climberTilt = new ClimberTilt();
  private TalonFX outerArmMotor;
  private double outerArmSpeed = 1.0;

  private boolean ignoreEncoder = true;
  private boolean resetOuterArmEncoder = false;

  private final String subsystemName = "Outer Arm Climber";

  private NetworkTable netTblOuterClimber = NetworkTableInstance.getDefault().getTable(subsystemName);

  private NetworkTableEntry entryOuterArmSpeed= netTblOuterClimber.getEntry("Outer Arm Speed");
  private NetworkTableEntry entryOuterArmMotorSpeed= netTblOuterClimber.getEntry("OuterArmMotorSpeed");
  private NetworkTableEntry entryResetOuterArmEncoder = netTblOuterClimber.getEntry("Reset Outer Arm Encoder");
  private NetworkTableEntry entryOuterArmPos = netTblOuterClimber.getEntry("Outer Encoder");
  private NetworkTableEntry entryIgnoreEncoder = netTblOuterClimber.getEntry("Ignore Encoder");
  public ClimberOuter() {
    setNetworkTableListeners();
    init();

    PIDCoef outerArmCoef = new PIDCoef(subsystemName, "Outer Arm", outerArmMotor, 0.15);

    SmartDashboard.putData(this);
  }
  public boolean getIgnoreEncoder(){
    return this.ignoreEncoder;
  }
  public boolean getAreOuterArmsTooLong(){
    // returns true when the limit switch is engaged and the outer arms exceed the encoder position
    return getOuterClimberPosition() >= ClimberConfig.OUTER_LEGAL_LIMIT && climberTilt.getIsTiltFullyRetracted();
}

  public void outerArmToPosition(double targetPosition){
    outerArmMotor.set(TalonFXControlMode.Position, targetPosition);
  }

  public double getOuterClimberPosition(){
    return outerArmMotor.getSelectedSensorPosition();
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
public void runOuterArmsToSpeed(double speed){
  outerArmMotor.set(ControlMode.PercentOutput, speed);
}

public void stopOuterArms(){
//outerArmSpeed = 0.0;
  outerArmMotor.set(ControlMode.PercentOutput, 0.0);
}

public void init(){
  int timeOut = 30;
  double maxPower = 0.8;
  int allowableError = 0;
  int pidIndex = 0;

  outerArmMotor = new TalonFX(CanBusConfig.INNER_ARM);
  outerArmMotor.clearStickyFaults();
  outerArmMotor.configFactoryDefault();
  outerArmMotor.setNeutralMode(NeutralMode.Brake);

    outerArmMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, pidIndex, timeOut);
    outerArmMotor.configNominalOutputForward(0, timeOut);
		outerArmMotor.configNominalOutputReverse(0, timeOut);
		outerArmMotor.configPeakOutputForward(maxPower, timeOut);
		outerArmMotor.configPeakOutputReverse(-maxPower, timeOut);
    outerArmMotor.configAllowableClosedloopError(pidIndex, allowableError, timeOut);

    outerArmMotor.config_kF(pidIndex, 0, timeOut);
    outerArmMotor.config_kP(pidIndex, 0.15, timeOut);
    outerArmMotor.config_kI(pidIndex, 0, timeOut);
    outerArmMotor.config_kD(pidIndex, 0, timeOut);
    outerArmMotor.setSelectedSensorPosition(0.0);
}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    entryOuterArmMotorSpeed.setDouble(outerArmMotor.getMotorOutputPercent());
    entryOuterArmPos.setDouble(outerArmMotor.getSelectedSensorPosition());
    entryOuterArmSpeed.setDouble(outerArmSpeed);
  }

  public void setNetworkTableListeners(){
    entryResetOuterArmEncoder.setBoolean(resetOuterArmEncoder);
    String resetOuterArmEncoderEntryName = NetworkTable.basenameKey(entryResetOuterArmEncoder.getName());
    netTblOuterClimber.addEntryListener(resetOuterArmEncoderEntryName, (table, key, entry, value, flags)->{
        if (value.getBoolean()){
            System.out.println("Resetting Outer Arm Encoder");
            outerArmMotor.set(TalonFXControlMode.PercentOutput, 0);
            outerArmMotor.setSelectedSensorPosition(0);
            resetOuterArmEncoder = false;
            entryResetOuterArmEncoder.setBoolean(resetOuterArmEncoder);
        }
    },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Outer Arm Speed", () -> this.outerArmSpeed, (value) -> this.outerArmSpeed = value);
  }
}
