package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;
import frc.robot.sim.PhysicsSim;

public class Climber2 extends SubsystemBase {
// **********************************************
// Class Variables
// **********************************************


// **********************************************
// Instance Variables
// **********************************************
private WPI_TalonFX vertMotor;
private WPI_TalonFX advanceMotor;
private WPI_TalonFX articulateMotor;

private Encoder vertEncoder;
private double vertPosition;

private boolean currentLimitOn = false;
private boolean limitEnableWasPressed = false;
private String canBusName = "";

private final XboxController xboxController;
private final XboxController.Axis articulateAxis;
private final XboxController.Axis advanceAxis;

private double climbSpeed = 0.1;
private double rateOfChange = .05;
private double articulateSpeed = 0.0;
private double advanceSpeed = 0.0;
private final double startSpeed = 0.1;

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
private NetworkTableEntry entryVertPosition= netTblClimber.getEntry("VertPosition");



// **********************************************
// Constructors
// **********************************************

public Climber2 (XboxController xboxController, XboxController.Axis articulateAxis, XboxController.Axis advanceAxis){
    System.out.println(String.format("Entering %s::%s", this.getClass().getSimpleName(), new Throwable().getStackTrace()[0].getMethodName()));
    
    this.xboxController = xboxController;
    this.articulateAxis = articulateAxis;
    this.advanceAxis = advanceAxis;

    vertMotor = new WPI_TalonFX(CanBusConfig.VERT_ARM, canBusName);
    advanceMotor = new WPI_TalonFX(CanBusConfig.ADVANCE_ARM, canBusName);
    articulateMotor = new WPI_TalonFX(CanBusConfig.ARTICULATOR, canBusName);

    vertPosition = vertMotor.getSelectedSensorPosition();


    // set factory default for all motors
    configMotor(vertMotor);
    configMotor(advanceMotor);
    configMotor(articulateMotor);


    // Configure the network table entries
    System.out.println(String.format("entryClimberSpeed.getName(): %s", entryClimberSpeed.getName()));
    netTblClimber.addEntryListener(climbSpeedEntryName, (table, key, entry, value, flags)->{
        System.out.println("The value for climber speed changed");
        if (climbSpeed != value.getDouble()){
            System.out.println("Updating the instance att based on table data");
            climbSpeed = value.getDouble();
        }
    },  EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    PhysicsSim.getInstance().addTalonFX(vertMotor, 0.5, 6800);
    PhysicsSim.getInstance().addTalonFX(articulateMotor, 0.5, 6800);
    PhysicsSim.getInstance().addTalonFX(advanceMotor, 0.5, 6800);

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
		/* Get joystick inputs */
		boolean btn1 = xboxController.getBackButton();
		boolean btn2 = xboxController.getStartButton();
		boolean limitEnableIsPressed = xboxController.getRightStickButton();
		double stick = -1.0 * xboxController.getRawAxis(XboxController.Axis.kLeftY.value);

		if (btn1) {
			/* on button 1 press, manual control with stick */
			vertMotor.set(ControlMode.PercentOutput, stick);
		} else if (btn2) {
			/* on button 2 press, servo back to zero */
			double targetPos = 0;
			vertMotor.set(ControlMode.Position, targetPos);
		} else {
			/* otherwise stop output */
			vertMotor.set(ControlMode.PercentOutput, 0);
		}

		/* on button5 (shoulder button on Logitech gamepad) */
		if (limitEnableIsPressed && !limitEnableWasPressed) {
			/* toggle current limit */
			currentLimitOn = !currentLimitOn;
			/* update Talon current limit */
			//_tal.enableCurrentLimit(_currentLimEn);
			/* print to DS */
			System.out.println("EnableCurrentLimit:"  + currentLimitOn);
            configMotor(vertMotor);
		} else if (!limitEnableIsPressed && limitEnableWasPressed){
            currentLimitOn = false;
            configMotor(vertMotor);
        }

		/* save button state for next loop */
		limitEnableWasPressed = limitEnableIsPressed;

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

        entryCurrentLimitEnabled.setBoolean(currentLimitOn);
        entryVertPosition.setDouble(vertMotor.getSelectedSensorPosition());

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

    private void configMotor(WPI_TalonFX motor){
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
        /* Gains for Position Closed Loop servo */
        motor.config_kP(0, 2.0, 30);
        motor.config_kI(0, 0.0, 30);
        motor.config_kD(0, 0.0, 30);
        motor.config_kF(0, 0.0, 30);

            // set braking mode
        motor.setNeutralMode(NeutralMode.Brake);

        if (this.currentLimitOn){
            // set Current Limits
            double supplyLimit = 10;
            double supplyTriggerThreshold = 15;
            double supplyTriggerThresholdTime = 0.5;

            double statorLimit = 20;
            double statorTriggerThreshold = 25;
            double statorTriggerThresholdTime = 1.0;

            SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, supplyLimit, supplyTriggerThreshold, supplyTriggerThresholdTime);
            // StatorCurrentLimitConfiguration statorCurrentLimitConfiguration = new StatorCurrentLimitConfiguration(true, statorLimit, statorTriggerThreshold, statorTriggerThresholdTime);
            motor.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
            // motor.configStatorCurrentLimit(statorCurrentLimitConfiguration);
        }
    }
    
}
