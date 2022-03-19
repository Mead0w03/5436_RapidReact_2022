package frc.robot.subsystems;

import java.util.function.BooleanSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CanBusConfig;

public class Intake extends SubsystemBase {
    private CANSparkMax intakeCargo;
    private CANSparkMax intakeRetractLeft;
    private CANSparkMax intakeRetractRight;
    private CANSparkMax intakeStorage;
    private RelativeEncoder intakeCargoEncoder;
    private RelativeEncoder intakeRetractLeftEncoder;
    private RelativeEncoder intakeRetractRightEncoder;
    private RelativeEncoder intakeStorageEncoder;
    private SparkMaxPIDController intakeRetractLeftPID;
    private SparkMaxPIDController intakeRetractRightPID;
    private double intakeCargoSpeed = 0.5;
    private double intakeRetractSpeed = 0.1;
    private double kP = 0.2;
    private double kI = 0.0;
    public int flip = 0;
    public String retractMode = "Manual";
    //private double intakeSpeed = 0;

    NetworkTable intakeTable = NetworkTableInstance.getDefault().getTable(this.getClass().getSimpleName());
    NetworkTableEntry entryCargoSpeed = intakeTable.getEntry("Cargo Intake Speed");
    NetworkTableEntry entryRetractSpeed = intakeTable.getEntry("Retract Intake Speed");
    NetworkTableEntry entryCargoMotorPower = intakeTable.getEntry("Cargo Motor Power");
    NetworkTableEntry entryRetractLeftPower = intakeTable.getEntry("Left Retract Motor Power");
    NetworkTableEntry entryRetractRightPower = intakeTable.getEntry("Right Retract Motor Power");
    NetworkTableEntry entrySpeedInput = intakeTable.getEntry("Enter Input Speed");
    NetworkTableEntry entryRetractLeftEncoder = intakeTable.getEntry("Left Retract Encoder Speed");
    NetworkTableEntry entryRetractRightEncoder = intakeTable.getEntry("Right Retract Encoder Speed");
    NetworkTableEntry entryRetractMode = intakeTable.getEntry("Intake Retract Mode");

    public Intake(){
        //declaring motor controllers
        intakeCargo = new CANSparkMax(CanBusConfig.CARGO, MotorType.kBrushless);
        intakeRetractLeft = new CANSparkMax(CanBusConfig.RETRACT_LEFT, MotorType.kBrushless);
        intakeRetractRight = new CANSparkMax(CanBusConfig.RETRACT_RIGHT, MotorType.kBrushless);
        intakeStorage = new CANSparkMax(CanBusConfig.STORAGE, MotorType.kBrushless);

        //setting left motor to be follower of right + inverting left motor
        intakeRetractLeft.follow(intakeRetractRight, true);

        //setting idle motor mode
        intakeRetractLeft.setIdleMode(IdleMode.kBrake);
        intakeRetractRight.setIdleMode(IdleMode.kBrake);

        //setting encoders
        intakeCargoEncoder = intakeCargo.getEncoder();
        intakeRetractLeftEncoder = intakeRetractLeft.getEncoder();
        intakeRetractRightEncoder = intakeRetractRight.getEncoder();
        intakeStorageEncoder = intakeStorage.getEncoder();

        //setting PID controllers
        intakeRetractLeftPID = intakeRetractLeft.getPIDController();
        intakeRetractLeftPID.setOutputRange(-0.4, 0.4);
        intakeRetractLeftPID.setP(kP);
        intakeRetractLeftPID.setI(kI);
        intakeRetractLeftPID.setD(0.0);
        intakeRetractLeftPID.setFF(0.05);
        intakeRetractRightPID = intakeRetractRight.getPIDController();
        intakeRetractRightPID.setOutputRange(-0.4, 0.4);
        intakeRetractRightPID.setP(kP);
        intakeRetractRightPID.setI(kI);
        intakeRetractRightPID.setD(0.0);
        intakeRetractRightPID.setFF(0.05);

        intakeTable.addEntryListener("P coeficient", (table, key, entry, value, flags) -> {
            kP = value.getDouble();
            System.out.println(String.format("p changed value: %.2f", kP));
            intakeRetractLeftPID.setP(kP);
            intakeRetractRightPID.setP(kP);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    
        intakeTable.addEntryListener("I coeficient", (table, key, entry, value, flags) -> {
            kI = value.getDouble();
            System.out.println(String.format("I changed value: %.2f", kP));
            intakeRetractLeftPID.setI(kI);
            intakeRetractRightPID.setI(kI);
            }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        


        //SmartDashboard.putNumber("Intake Speed", intakeSpeed);

        entrySpeedInput.setDouble(0.0);

        String intakeCargoSpeedName = NetworkTable.basenameKey(entryCargoSpeed.getName());
        intakeTable.addEntryListener(intakeCargoSpeedName, (table, key, entry, value, flags) -> {
            System.out.println("Intake speed changed value: " + value.getValue());
            intakeCargoSpeed = value.getDouble();
         }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

         String intakeRetractSpeedName = NetworkTable.basenameKey(entryRetractSpeed.getName());
         intakeTable.addEntryListener(intakeRetractSpeedName, (table, key, entry, value, flags) -> {
             System.out.println("Retract speed changed value: " + value.getValue());
             intakeRetractSpeed = value.getDouble();
          }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

         REVPhysicsSim.getInstance().addSparkMax(intakeRetractRight, DCMotor.getNEO(1));
    }

    public void resetAllEncoders(){
        intakeCargoEncoder.setPosition(0);
        intakeRetractLeftEncoder.setPosition(0);
        intakeRetractRightEncoder.setPosition(0);
        intakeStorageEncoder.setPosition(0);
    }

    public void resetRetractEncoders(){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        intakeRetractLeftEncoder.setPosition(0);
        intakeRetractRightEncoder.setPosition(0);
    }

    public void cargoIn(){
        intakeCargo.set(intakeCargoSpeed);
        intakeStorage.set(intakeCargoSpeed);
    }

    public void cargoOut(){
        intakeCargo.set(-intakeCargoSpeed);
        intakeStorage.set(-intakeCargoSpeed);
    }

    public void cargoStop(){
        intakeCargo.set(0);
        intakeStorage.set(0);
    }

    public void intakeDown(){
        intakeRetractRight.set(-intakeRetractSpeed);
        //left is declared as follower
    }

    public void intakeUp(){
        intakeRetractRight.set(intakeRetractSpeed);
        //left is declared as follower
    }

    public void intakeStop(){
        if(retractMode !="PID"){
            intakeRetractRight.set(0);
        }
        //left is declared as follower
    }

    public void intakeMove(String direction, String type){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        int d = 0;

        if(type == "PID"){
            System.out.println("Entering PID Control");
            if(direction.equalsIgnoreCase("Up")){
                intakeRetractRightPID.setReference(0, CANSparkMax.ControlType.kPosition);
                //intakeRetractLeftPID.setReference(0, CANSparkMax.ControlType.kPosition);
            } else if(direction.equalsIgnoreCase("Down")){
                intakeRetractRightPID.setReference(Constants.IntakeConfig.RETRACT_ARM_DOWN_POSITION, CANSparkMax.ControlType.kPosition);
                //intakeRetractLeftPID.setReference(Constants.IntakeConfig.RETRACT_ARM_DOWN_POSITION, CANSparkMax.ControlType.kPosition);
            } else {
                System.out.println("Invalid type/direction parameter sent.");
            }
        } else if (type == "Manual"){
            System.out.println("Entering Manual Mode");
            if(direction.equalsIgnoreCase("Up")){
                intakeRetractRight.set(intakeRetractSpeed);
            } else if(direction.equalsIgnoreCase("Down")){
                intakeRetractRight.set(-intakeRetractSpeed);
            } else {
                System.out.println("Invalid type/direction parameter sent.");
            }
        }
    }

    public void changeIntakeMode(){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        int i;
        flip++; //increasing flip variable
        i = flip % 2;
        if(i > 0){
            retractMode = "PID";
        } else {
            retractMode = "Manual";
        }
    }

    public BooleanSupplier isRetractModePID(){
         return ()-> retractMode.equals("PID");
    }


    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        //SmartDashboard.putNumber("Intake Speed", intakeSpeed);
        
        entryCargoSpeed.setDouble(intakeCargoSpeed);
        entryRetractSpeed.setDouble(intakeRetractSpeed);
        entryCargoMotorPower.setDouble(intakeCargo.get());
        entryRetractLeftPower.setDouble(intakeRetractLeft.get());
        entryRetractRightPower.setDouble(intakeRetractRight.get());
        entryRetractLeftEncoder.setDouble(intakeRetractLeftEncoder.getPosition());
        entryRetractRightEncoder.setDouble(intakeRetractRightEncoder.getPosition());
        entryRetractMode.setString(retractMode);

        // double inputSpeedValue = entrySpeedInput.getDouble(0);
        //intakeSpeed = inputSpeedValue;
    }

    @Override
    public void register() {
        // TODO Auto-generated method stub
        super.register();
    }

    @Override
    public void simulationPeriodic() {
        REVPhysicsSim.getInstance().run();
    }

}
