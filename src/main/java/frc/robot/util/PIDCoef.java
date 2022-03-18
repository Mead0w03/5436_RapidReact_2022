// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class PIDCoef implements Sendable{
    public String subsystemName = "";
    public String motorName = "";
    private Object motor = null;
    public double kP = 0.0;
    public double kI = 0.0;
    public double kD = 0.0;
    public double kF = 0.0;

    public enum Coeff{
        kP, 
        kI, 
        kD, 
        kF;
    }

    public PIDCoef(){
        System.out.println("PIDCoeff instantiated");
        registerSendable();
    }

    public PIDCoef(String subsystemName, String motorName, Object motor, double kP){
        this.subsystemName = subsystemName;
        this.motorName = motorName;
        this.motor = motor;
        this.kP = kP;
        registerSendable();
    }

    public PIDCoef(String subsystemName, String motorName, Object motor, double kP, double kI, double kD, double kF){
        this.subsystemName = subsystemName;
        this.motorName = motorName;
        this.motor = motor;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        registerSendable();
    }

    private void registerSendable(){
        SendableRegistry.addLW(this, subsystemName, String.join("/", this.subsystemName, this.motorName));
        SmartDashboard.putData(this);
    }

    private void setCoeff(Coeff coeff, TalonFX talonFX, double setValue){
        if (coeff == Coeff.kP){
            talonFX.config_kP(0, setValue);
        } else if (coeff == Coeff.kI){
            talonFX.config_kI(0, setValue);
        } else if(coeff == Coeff.kD){
            talonFX.config_kD(0, setValue);
        } else if(coeff == Coeff.kF){
            talonFX.config_kF(0, setValue);
        }
        System.out.printf("Motor %s value for %s updated to %.2f\n", this.motorName, coeff.name(), setValue);;
    }

    private void setCoeff(Coeff coeff, CANSparkMax canSparkMax, double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        SparkMaxPIDController pidController= canSparkMax.getPIDController();
        if (coeff == Coeff.kP){
            pidController.setP(setValue);
        } else if (coeff == Coeff.kI){
            pidController.setI(setValue);
        } else if(coeff == Coeff.kD){
            pidController.setD(setValue);
        } else if(coeff == Coeff.kF){
            pidController.setFF(setValue);
        }
        System.out.printf("Motor %s value for %s updated to %.2f\n", this.motorName, coeff.name(), setValue);;
    }

    private boolean setCoeffForMotorType(Coeff coeff, Object motor, double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        System.out.printf("**Motor %s %s value is being changed...\n", this.motorName, coeff.name());
        
        // block execution if motor is null
        if (this.motor == null){
            System.out.println("**Exiting setKp because motor is null!!");
            return false;
        }

        boolean valueSet = true;
        if(this.motor.getClass() == TalonFX.class){
            TalonFX talonFX = (TalonFX) this.motor;
            setCoeff(coeff, talonFX, setValue);
        } else if (this.motor.getClass() == CANSparkMax.class){
            CANSparkMax canSparkMax = (CANSparkMax) motor;
            setCoeff(coeff, canSparkMax, setValue);
        } else{
            valueSet = false;
        }

        return valueSet;
    }

    private void setKp(double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        boolean valueSet = setCoeffForMotorType(Coeff.kP, motor, setValue);
        if (valueSet) this.kP = setValue;
    }

    private void setKi(double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        boolean valueSet = setCoeffForMotorType(Coeff.kI, motor, setValue);
        if (valueSet) this.kI = setValue;
    }

    private void setKd(double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        boolean valueSet = setCoeffForMotorType(Coeff.kD, motor, setValue);
        if (valueSet) this.kD = setValue;
    }

    private void setKf(double setValue){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        boolean valueSet = setCoeffForMotorType(Coeff.kF, motor, setValue);
        if (valueSet) this.kF = setValue;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        System.out.println("Running initSendable in PIDCoef");
        builder.addStringProperty("Motor Name", () -> this.motorName, null);
        builder.addDoubleProperty("_kP", () -> this.kP, (value) -> setKp(value));
        builder.addDoubleProperty("_kI", () -> this.kI, (value) -> setKi(value));
        builder.addDoubleProperty("_kD", () -> this.kD, (value) -> setKd(value));
        builder.addDoubleProperty("_kF", () -> this.kF, this::setKf);
    }

}
