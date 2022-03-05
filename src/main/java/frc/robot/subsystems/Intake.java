package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
//import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;

public class Intake extends SubsystemBase {
    private CANSparkMax intake;
    private CANSparkMax intakeRetractLeft;
    private CANSparkMax intakeRetractRight;
    private CANSparkMax intakeCargo;
    private RelativeEncoder intakeCargoEncoder;
    private RelativeEncoder intakeRetractLeftEncoder;
    private RelativeEncoder intakeRetractRightEncoder;
    private RelativeEncoder intakeStorageEncoder;
    private double intakeCargoSpeed = 0.5;
    private double intakeUpSpeed = 0.5;
    private double intakeDowned = 0.5;3
    //private RelativeEncoder encoder = intakeRetractLeft.getEncoder();
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

    public Intake(){
        intake = new CANSparkMax(CanBusConfig.CARGO, MotorType.kBrushless);
        intakeRetractLeft = new CANSparkMax(CanBusConfig.RETRACT_LEFT, MotorType.kBrushless);
        intakeRetractRight = new CANSparkMax(CanBusConfig.RETRACT_RIGHT, MotorType.kBrushless);
        intakeCargo = new CANSparkMax(CanBusConfig.STORAGE, MotorType.kBrushless);

        intakeCargoEncoder = intake.getEncoder();
        intakeRetractLeftEncoder = intakeRetractLeft.getEncoder();
        intakeRetractRightEncoder = intakeRetractRight.getEncoder();
        intakeStorageEncoder = intakeCargo.getEncoder();

        intakeCargoEncoder.setPosition(0);
        intakeRetractLeftEncoder.setPosition(0);
        intakeRetractRightEncoder.setPosition(0);
        intakeStorageEncoder.setPosition(0);

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
             intakeDownSpeed = value.getDouble();
          }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

         REVPhysicsSim.getInstance().addSparkMax(intakeRetractRight, DCMotor.getNEO(1));
    }

    public void intakeIn(){
        intake.set(intakeCargoSpeed);
    }

    public void intakeCargoIn(){
        intakeCargo.set(intakeCargoSpeed);
    }

    public void intakeOut(){
        intake.set(-intakeCargoSpeed);
    }

    public void intakeCargoOut(){
        intakeCargo.set(-intakeCargoSpeed);
    }

    public void intakeCargoStop(){
        intakeCargo.set(0);
    }

    public void intakeStop(){
        intake.set(0);
    }

    public void intakeLiftUp(){
        intakeRetractLeft.set(intakeUpSpeed);
        intakeRetractRight.set(-intakeUpSpeed);
    }

    public void intakeLiftDown(){
        intakeRetractLeft.set(-intakeDowned);
        intakeRetractRight.set(intakeDowned);
    }

    public void intakeLiftStop(){
        intakeRetractLeft.set(0);
        intakeRetractRight.set(0);
    }


    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        //SmartDashboard.putNumber("Intake Speed", intakeSpeed);
        
        entryCargoSpeed.setDouble(intakeCargoSpeed);
        entryRetractSpeed.setDouble(intakeDownSpeed);
        entryCargoMotorPower.setDouble(intake.get());
        entryRetractLeftPower.setDouble(intakeRetractLeft.get());
        entryRetractRightPower.setDouble(intakeRetractRight.get());
        entryRetractLeftEncoder.setDouble(intakeRetractLeftEncoder.getPosition());
        entryRetractRightEncoder.setDouble(intakeRetractRightEncoder.getPosition());

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
