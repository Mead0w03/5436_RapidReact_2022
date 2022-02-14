package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;
import frc.robot.sim.PhysicsSim;

public class Climber extends SubsystemBase {
    // **********************************************
    // Class Variables
    // **********************************************


    // **********************************************
    // Instance Variables
    // **********************************************
    private WPI_TalonFX vertMotor;
    private WPI_TalonFX advanceMotor;
    private WPI_TalonFX articulateMotor;
    private List<TalonFX> climberMotors = new ArrayList<>();

    private final XboxController xboxController;
    private final XboxController.Axis articulateAxis;
    private final XboxController.Axis advanceAxis;

    private double climbSpeed = 0.1;
    private double rateOfChange = .05;
    private double articulateSpeed = 0.0;
    private double advanceSpeed = 0.0;
    private final double startSpeed = 0.1;

    private boolean currentLimitEnabled = false;
    private double vertForwardLimitValue = 50000;
    private double vertReverseLimitValue = 0;

    private double peakVertCurrent;
    private double peakAdvanceCurrent;
    private double peakArticulateCurrent;

    private final String netTblName = this.getClass().getSimpleName();
    private NetworkTable netTblClimber = NetworkTableInstance.getDefault().getTable(netTblName);

    private NetworkTableEntry entryCurrentCommand= netTblClimber.getEntry("Climber Command");
    private final String climbSpeedEntryName = "Climber Speed";

    private NetworkTableEntry entryClimberSpeed= netTblClimber.getEntry(climbSpeedEntryName);
    private NetworkTableEntry entryArticulateSpeed= netTblClimber.getEntry("Articulate Speed");
    private NetworkTableEntry entryAdvanceSpeed= netTblClimber.getEntry("Advance Speed");

    private NetworkTableEntry entryVertMotorSpeed= netTblClimber.getEntry("VertMotorSpeed");
    private NetworkTableEntry entryAdvanceMotorSpeed= netTblClimber.getEntry("AdvanceMotorSpeed");
    private NetworkTableEntry entryArticulateMotorSpeed= netTblClimber.getEntry("ArticulateMotorSpeed");
    
    private NetworkTableEntry entryVertMotorCurrent= netTblClimber.getEntry("VertMotorCurrent");
    private NetworkTableEntry entryAdvanceMotorCurrent= netTblClimber.getEntry("AdvanceMotorCurrent");
    private NetworkTableEntry entryArticulateMotorCurrent= netTblClimber.getEntry("ArticulateMotorCurrent");

    private NetworkTableEntry entryCurrentLimitEnabled= netTblClimber.getEntry("CurrentLimitEnabled");
    private NetworkTableEntry entryPeakVertCurrent = netTblClimber.getEntry("PeakVertCurrent");
    private NetworkTableEntry entryPeakAdvanceCurrent = netTblClimber.getEntry("PeakAdvanceCurrent");
    private NetworkTableEntry entryPeakArticulateCurrent = netTblClimber.getEntry("PeakArticulateCurrent");
    
    private NetworkTableEntry entryVertPosition = netTblClimber.getEntry("VertMotorPosition");
    private NetworkTableEntry entryAdvancePosition = netTblClimber.getEntry("AdvanceMotorPosition");
    private NetworkTableEntry entryArticulatePosition = netTblClimber.getEntry("ArticulateMotorPosition");
    
    private NetworkTableEntry entryVertForwardLimit = netTblClimber.getEntry("VertForwardLimit");
    private NetworkTableEntry entryVertReverseLimit = netTblClimber.getEntry("VertReverseLimit");


    // **********************************************
    // Constructors
    // **********************************************

    public Climber (XboxController xboxController, XboxController.Axis articulateAxis, XboxController.Axis advanceAxis){
        System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        this.xboxController = xboxController;
        this.articulateAxis = articulateAxis;
        this.advanceAxis = advanceAxis;

        vertMotor = new WPI_TalonFX(CanBusConfig.VERT_ARM);
        advanceMotor = new WPI_TalonFX(CanBusConfig.ADVANCE_ARM);
        articulateMotor = new WPI_TalonFX(CanBusConfig.ARTICULATOR);

        climberMotors.add(vertMotor);
        climberMotors.add(advanceMotor);
        climberMotors.add(articulateMotor);

        climberMotors.forEach(m->configMotor(m));

        // leftEncoder = leftMotor.getEncoder();
        // rightEncoder = rightMotor.getEncoder();

        // Create listener for climbSpeed
        System.out.println(String.format("entryClimberSpeed.getName(): %s", entryClimberSpeed.getName()));
        netTblClimber.addEntryListener(climbSpeedEntryName, (table, key, entry, value, flags)->{
            System.out.println("The value for climber speed changed");
            if (climbSpeed != value.getDouble()){
                System.out.println("Updating the instance att based on table data");
                climbSpeed = value.getDouble();
            }
        },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // Create listener for 
        String entryName = NetworkTable.basenameKey(entryCurrentLimitEnabled.getName());
        System.out.println(String.format("entryCurrentLimitEnabled.getName(): %s", entryName));
        netTblClimber.addEntryListener(entryName, (table, key, entry, value, flags)->{
            if (currentLimitEnabled != value.getBoolean()){
                System.out.println("Updating currentLimitEnabled to " + value.getBoolean());
                currentLimitEnabled = value.getBoolean();
                // reconfigure the motors
                climberMotors.forEach(m->configMotor(m));
            }
        },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // Create listener for vertForwardLimit
        entryName = NetworkTable.basenameKey(entryVertForwardLimit.getName());
        System.out.println(String.format("Creating listener for: %s", entryName));
        netTblClimber.addEntryListener(entryName, (table, key, entry, value, flags)->{
            if (vertForwardLimitValue != value.getDouble()){
                System.out.println("Updating vertForwardLimit to " + value.getDouble());
                vertForwardLimitValue = value.getDouble();
                // reconfigure the motors
                setSoftLimits(vertMotor, vertForwardLimitValue, vertReverseLimitValue);;
            }
        },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        
        // Create listener for vertReverseLimit
        entryName = NetworkTable.basenameKey(entryVertReverseLimit.getName());
        System.out.println(String.format("Creating listener for: %s", entryName));
        netTblClimber.addEntryListener(entryName, (table, key, entry, value, flags)->{
            if (vertReverseLimitValue != value.getDouble()){
                System.out.println("Updating vertReverselimitValue to " + value.getDouble());
                vertReverseLimitValue = value.getDouble();
                // reconfigure the motors
                setSoftLimits(vertMotor, vertForwardLimitValue, vertReverseLimitValue);
            }
        },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    // **********************************************
    // Getters & Setters
    // **********************************************


    // **********************************************
    // Class Methods
    // **********************************************


    // **********************************************
    // Instance Methods
    // **********************************************

    private void configMotor(TalonFX motor){
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
        /* Gains for Position Closed Loop servo */
        motor.config_kP(0, 2.0, 30);
        motor.config_kI(0, 0.0, 30);
        motor.config_kD(0, 0.0, 30);
        motor.config_kF(0, 0.0, 30);

            // set braking mode
        motor.setNeutralMode(NeutralMode.Brake);
        setSoftLimits(vertMotor, vertForwardLimitValue, vertReverseLimitValue);

        if (this.currentLimitEnabled){
            // set Current Limits
            double supplyLimit = 10;
            double supplyTriggerThreshold = 15;
            double supplyTriggerThresholdTime = 0.5;

            SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, supplyLimit, supplyTriggerThreshold, supplyTriggerThresholdTime);
            motor.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        }

    }

    private void setSoftLimits(TalonFX motor, double forwardLimit, double reverseLimit){
        motor.configForwardSoftLimitEnable(true, 30);
        motor.configReverseSoftLimitEnable(true, 30);
        // motor.configForwardLimitSwitchSource(TalonFXFeedbackDevice.IntegratedSensor, LimitSwitchNormal.NormallyOpen, 30);
        motor.configForwardSoftLimitThreshold(forwardLimit);
        motor.configReverseSoftLimitThreshold(reverseLimit);
    }

    public void resetSpeed(){
        climbSpeed = startSpeed;
    }

    public void ascend(){
        // todo:make speed variable
        vertMotor.set(ControlMode.PercentOutput, climbSpeed);
        // rightMotor.set(climbSpeed);
    }

    public void descend(){
        vertMotor.set(ControlMode.PercentOutput, -climbSpeed);
        // rightMotor.set(-climbSpeed);
    }

    public void stop(){
        vertMotor.set(ControlMode.PercentOutput, 0.0);
        // rightMotor.set(0);
    }

    public void increaseSpeed(){
        double newSpeed = climbSpeed += rateOfChange;
        if(newSpeed > 1.0) {
            newSpeed = 1;
        } else if(newSpeed < 0){
            newSpeed = rateOfChange;
        }
        climbSpeed = newSpeed;
    }

    public void decreaseSpeed(){
        double newSpeed = climbSpeed -= rateOfChange;
        if(newSpeed <= 0.0){
            newSpeed = rateOfChange;
        } else if (newSpeed >1){
            newSpeed = 1;
        }
        climbSpeed = newSpeed;
    }

    public void articulate(){
        // limits speed between 0.1 - 0.5
        articulateSpeed = xboxController.getRawAxis(articulateAxis.value) / 2.0;
        articulateMotor.set(ControlMode.PercentOutput, articulateSpeed);
    }

    public void stopArticulate(){
        articulateSpeed = 0.0;
        articulateMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void advance(){
        // limits speed between 0.1 - 0.5
        advanceSpeed = xboxController.getRawAxis(advanceAxis.value) / 2.0;
        advanceMotor.set(ControlMode.PercentOutput, advanceSpeed);
    }

    public void stopAdvance(){
        advanceSpeed = 0.0;
        advanceMotor.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public void periodic() {
        // System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
        
        entryCurrentCommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
        entryClimberSpeed.setDouble(climbSpeed);
        // entryRightMotorSpeed.setDouble(rightMotor.get());
        entryVertMotorSpeed.setDouble(vertMotor.getMotorOutputPercent());
        entryAdvanceMotorSpeed.setDouble(advanceMotor.getMotorOutputPercent());
        entryArticulateMotorSpeed.setDouble(articulateMotor.getMotorOutputPercent());
        
        entryVertMotorCurrent.setDouble(vertMotor.getSupplyCurrent());
        entryAdvanceMotorCurrent.setDouble(advanceMotor.getSupplyCurrent());
        entryArticulateMotorCurrent.setDouble(articulateMotor.getSupplyCurrent());
        entryArticulateSpeed.setDouble(articulateSpeed);
        entryAdvanceSpeed.setDouble(advanceSpeed);

        entryCurrentLimitEnabled.setBoolean(currentLimitEnabled);

        // update the peak current
        double currentVertSupplyCurrent = Math.abs(vertMotor.getSupplyCurrent());
        double currentAdvanceSupplyCurrent = Math.abs(advanceMotor.getSupplyCurrent());
        double currentArticulateSupplyCurrent = Math.abs(articulateMotor.getSupplyCurrent());
        peakVertCurrent = (currentVertSupplyCurrent > peakVertCurrent) ? currentVertSupplyCurrent : peakVertCurrent;
        peakAdvanceCurrent = (currentAdvanceSupplyCurrent > peakAdvanceCurrent) ? currentAdvanceSupplyCurrent : peakAdvanceCurrent;
        peakArticulateCurrent = (currentArticulateSupplyCurrent > peakArticulateCurrent) ? currentArticulateSupplyCurrent : peakArticulateCurrent;
        entryPeakVertCurrent.setString(String.format("%.2f", peakVertCurrent));
        entryPeakAdvanceCurrent.setString(String.format("%.2f", peakAdvanceCurrent));
        entryPeakArticulateCurrent.setString(String.format("%.2f", peakArticulateCurrent));

        //update encoder positions
        entryVertPosition.setString(String.format("%.0f", vertMotor.getSelectedSensorPosition()));
        entryAdvancePosition.setString(String.format("%.0f", advanceMotor.getSelectedSensorPosition()));
        entryArticulatePosition.setString(String.format("%.0f", articulateMotor.getSelectedSensorPosition()));

        entryVertForwardLimit.setDouble(vertForwardLimitValue);
        entryVertReverseLimit.setDouble(vertReverseLimitValue);


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

    public void initSimulation(){
        System.out.println("Adding motors to simulation");
        PhysicsSim.getInstance().addTalonFX(vertMotor, 0.5, 6800);
        PhysicsSim.getInstance().addTalonFX(articulateMotor, 0.5, 6800);
        PhysicsSim.getInstance().addTalonFX(advanceMotor, 0.5, 6800);
    }
    
}
