package frc.robot.subsystems;


import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CanBusConfig;

public class DriveBase extends SubsystemBase {

    //drive train motor instance variables
    private CANSparkMax motorFL;
    private CANSparkMax motorFR;
    private CANSparkMax motorBL;
    private CANSparkMax motorBR;

    //Gyro
    private PigeonIMU gyro = new PigeonIMU(CanBusConfig.GYRO);

    //deadband instance variables
    private double xAxisDeadband;
    private double yAxisDeadband;

    //Network Table instances
    NetworkTable driveBaseTable = NetworkTableInstance.getDefault().getTable(this.getClass().getSimpleName());
    NetworkTableEntry entryDriveBaseCurrentCommand = driveBaseTable.getEntry("Drive Base Current Command");
    NetworkTableEntry entryFREncoder = driveBaseTable.getEntry("FR Encoder");
    NetworkTableEntry entryFLEncoder = driveBaseTable.getEntry("FL Encoder");
    NetworkTableEntry entryBREncoder = driveBaseTable.getEntry("BR Encoder");
    NetworkTableEntry entryBLEncoder = driveBaseTable.getEntry("BL Encoder");

    //TODO: need to create deaden parameter to change during testing

    /** DriveBase Constructor
     * initializes brushless motor drive train on can ids 1-4
     * @param NetworkTableInstance 
     */
    public DriveBase() {
      motorFR = new CANSparkMax(CanBusConfig.FRONT_RIGHT, MotorType.kBrushless); 
      motorFL = new CANSparkMax(CanBusConfig.FRONT_LEFT, MotorType.kBrushless);
      motorBR = new CANSparkMax(CanBusConfig.BACK_RIGHT, MotorType.kBrushless);
      motorBL = new CANSparkMax(CanBusConfig.BACK_LEFT, MotorType.kBrushless);

      motorFR.setIdleMode(IdleMode.kBrake);
      motorFL.setIdleMode(IdleMode.kBrake);
      motorBR.setIdleMode(IdleMode.kBrake);
      motorBL.setIdleMode(IdleMode.kBrake);

      motorFR.setInverted(true);
      motorBR.setInverted(true);
    } //end of DriveBase() constructor

    /**
     * 
     * @param input - input from joystick (x or y axis)
     * @param deadband - desired sensitivy of joystick (joystick range of motion where driver feels comfortable with control over bot)
     * @return ouput after deadband
     */
    public double deaden(double input, double deadband) {
        deadband = Math.min(1, deadband);
        deadband = Math.max(0, deadband);
       
        if (Math.abs(input) - deadband < 0) {
          return 0;
        }
        else {
          return Math.signum(input) * ((Math.abs(input) - deadband) / (1 - deadband));
        }
    }// end of deaden() method
      
    /**
     * i
     */
    public void init() {

        motorFL.clearFaults();
        motorBL.clearFaults();
        motorFR.clearFaults();
        motorBR.clearFaults();
    }  //End of init method
  
  
  
  
  
    /**
     * 
     * @param l - speed of left motors on drivetrain (% output)
     * @param r - speed of right motors on drivetrain (% output)
     */
    public void drive(double l, double r){
      //Drive Command to send values to DriveBase Motors
    
      motorFL.set(l);
      motorBL.set(l);
      motorFR.set(r);
      motorBR.set(r);
    }  //End of drive() 
  
    public double[] driveProcessing(double xAxis, double yAxis, String direction){
        double returnValue[] = {0,0};
        double strafe = xAxis;
        double fwd = -yAxis;  //Invert Flightstick values as forward is negative
    
        //First check to see if input is within deadzone
        strafe = deaden(strafe, xAxisDeadband);
        fwd = deaden(fwd, yAxisDeadband);
        
        //Bind the output to insure it is not above 100%
        double L = boundSpeed(fwd + strafe);
        double R = boundSpeed(fwd - strafe);
    
        // R = Robot.rightSideRequiresInversion? -R: R;  
        // I inverted motor controller in constructor for right side so no need to invert right side
        
        returnValue[0] = L;
        returnValue[1] = R;
        return returnValue;
    } // End of driveProcessing

    //Quick Binding of speed to not go above 100%
  public double boundSpeed(double inputSpeed){
    return (Math.abs(inputSpeed) >= 1? Math.signum(inputSpeed)*1:inputSpeed);
  } // end of driveProcessing()
  
    public void stopAllDrivetrainMotors(){
      motorFL.set(0);
      motorFR.set(0);
      motorBL.set(0);
      motorBR.set(0);
    }//End of stopAllDrivetrainMotors()

    public double getFrontRightEncoderValue(){
      motorFR.getEncoder().setPosition(0.0);
      
      return motorFR.getEncoder().getPosition();
    }

    public double getBackRightEncoderValue(){
      motorBR.getEncoder().setPosition(0.0);
      
      return motorBR.getEncoder().getPosition();
    }

    public double getFrontLeftEncoderValue(){
      motorFR.getEncoder().setPosition(0.0);
      return motorFL.getEncoder().getPosition();
    }

    public double getBackLeftEncoderValue(){
      motorBL.getEncoder().setPosition(0.0);
      return motorBL.getEncoder().getPosition();
    }

    @Override
  public void periodic() {
    //Get the current command running on the subsystem
    entryDriveBaseCurrentCommand.setString((this.getCurrentCommand() == null) ? "None" : this.getCurrentCommand().getName());
    // This method will be called once per scheduler run
    entryFREncoder.setDouble(getFrontRightEncoderValue());
    entryBREncoder.setDouble(getBackRightEncoderValue());
    entryFLEncoder.setDouble(getFrontLeftEncoderValue());
    entryBLEncoder.setDouble(getBackLeftEncoderValue());
  }
    
}

