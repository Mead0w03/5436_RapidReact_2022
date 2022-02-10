package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private CANSparkMax intakeCargo;
    private CANSparkMax intakeRetractLeft;
    private CANSparkMax intakeRetractRight;
    private RelativeEncoder intakeCargoEncoder;
    private RelativeEncoder intakeRetractLeftEncoder;
    private RelativeEncoder intakeRetractRightEncoder;
    private double intakeCargoSpeed = 0.5;
    private double intakeRetractSpeed = 0.1;
    private double intakeSpeed = 0;

    NetworkTable intakeTable = NetworkTableInstance.getDefault().getTable(this.getClass().getSimpleName());
    NetworkTableEntry entryCargoSpeed = intakeTable.getEntry("Cargo Intake Speed");
    NetworkTableEntry entryRetractSpeed = intakeTable.getEntry("Retract Intake Speed");
    NetworkTableEntry entryCargoMotorPower = intakeTable.getEntry("Cargo Motor Power");
    NetworkTableEntry entryRetractLeftPower = intakeTable.getEntry("Left Retract Motor Power");
    NetworkTableEntry entryRetractRightPower = intakeTable.getEntry("Right Retract Motor Power");
    NetworkTableEntry entrySpeedInput = intakeTable.getEntry("Enter Input Speed");

    public Intake(){
        intakeCargo = new CANSparkMax(0, MotorType.kBrushless);
        intakeRetractLeft = new CANSparkMax(1, MotorType.kBrushless);
        intakeRetractRight = new CANSparkMax(2, MotorType.kBrushless);
        intakeCargoEncoder = intakeCargo.getEncoder();
        intakeRetractLeftEncoder = intakeRetractLeft.getEncoder();
        intakeRetractLeftEncoder = intakeRetractRight.getEncoder();

        SmartDashboard.putNumber("Intake Speed", intakeSpeed);

        entrySpeedInput.setDouble(0.0);

    }

    public void cargoIn(){
        intakeCargo.set(intakeCargoSpeed);
    }

    public void cargoOut(){
        intakeCargo.set(-intakeCargoSpeed);
    }

    public void cargoStop(){
        intakeCargo.set(0);
    }

    public void intakeUp(){
        intakeRetractLeft.set(intakeRetractSpeed);
        intakeRetractRight.set(-intakeRetractSpeed);
    }

    public void intakeDown(){
        intakeRetractLeft.set(-intakeRetractSpeed);
        intakeRetractRight.set(intakeRetractSpeed);
    }

    public void intakeStop(){
        intakeRetractLeft.set(0);
        intakeRetractRight.set(0);
    }



    @Override
    public void periodic() {
        // TODO Auto-generated method stub
        SmartDashboard.putNumber("Intake Speed", intakeSpeed);
        
        entryCargoSpeed.setDouble(intakeCargoSpeed);
        entryRetractSpeed.setDouble(intakeRetractSpeed);
        entryCargoMotorPower.setDouble(intakeCargo.get());
        entryRetractLeftPower.setDouble(intakeRetractLeft.get());
        entryRetractRightPower.setDouble(intakeRetractRight.get());

        double inputSpeedValue = entrySpeedInput.getDouble(0);
        //intakeSpeed = inputSpeedValue;
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
