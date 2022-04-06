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
  private TalonFX outerArmMotor;
  private double outerArmSpeed = 1.0;

  private boolean ignoreEncoder = true;
  private boolean resetOuterArmEncoder = false;

 
  public ClimberOuter() {
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));

    init();

    PIDCoef outerArmCoef = new PIDCoef(this.getClass().getSimpleName(), "Outer Arm", outerArmMotor, 0.15);

    SmartDashboard.putData(this);
  }

  public boolean getIgnoreEncoder(){
    return this.ignoreEncoder;
  }
//   public boolean getAreOuterArmsTooLong(){
//     // returns true when the limit switch is engaged and the outer arms exceed the encoder position
//     return getOuterClimberPosition() >= ClimberConfig.OUTER_LEGAL_LIMIT && climberTilt.getIsTiltFullyRetracted();
// }

  public void outerArmToPosition(double targetPosition){
    outerArmMotor.set(TalonFXControlMode.Position, targetPosition);
  }

  public double getOuterClimberPosition(){
    return outerArmMotor.getSelectedSensorPosition();
  }
  public void startOuterArms(String direction){
    // limits speed between 0.1 - 0.5
    //outerArmSpeed = xboxController.getRawAxis(outerArmAxis.value) / 2.0;
    double speed = 0.5;

    if(direction.equalsIgnoreCase("forward")){
        speed = outerArmSpeed;
    } else if(direction.equalsIgnoreCase("retract")) {
       speed = -outerArmSpeed;
    }
    outerArmMotor.set(ControlMode.PercentOutput, speed);
}

public void runOuterArmsToSpeed(double speed){
  outerArmMotor.set(ControlMode.PercentOutput, speed);
  outerArmSpeed = speed;
}

public void stopOuterArms(){
//outerArmSpeed = 0.0;
  outerArmMotor.set(ControlMode.PercentOutput, 0.0);
  outerArmSpeed = 0;
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

}

private void resetOuterArmEncoder(boolean resetFlag){
    // Reset Tilt Encoder when the value on the SmartDashboard is changed to True
    System.out.println("Resetting Outer Arm Encoder");
    // stop the motor
    outerArmMotor.set(TalonFXControlMode.PercentOutput, 0);
    // reset the encoders
    outerArmMotor.setSelectedSensorPosition(0);
    // set the flag back to false
    resetOuterArmEncoder = false;
}

  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Outer Arm Speed", () -> this.outerArmSpeed, null);
    builder.addDoubleProperty("Outer Arm Encoder", () -> outerArmMotor.getSelectedSensorPosition(), null);
    builder.addBooleanProperty("Reset Outer Arm Encoder", () -> resetOuterArmEncoder, this::resetOuterArmEncoder);
    builder.addBooleanProperty("Ignore Outer Arm Encoder", () -> ignoreEncoder, value -> {ignoreEncoder = value; System.out.printf("Setting outerArm ignoreEncoder %s\n", ignoreEncoder);});
    builder.addStringProperty("Outer Arm Command", () -> (this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName(), null);

  }
}
