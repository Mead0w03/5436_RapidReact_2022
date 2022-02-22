package frc.robot.subsystems;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveBase extends SubsystemBase {
    private CANSparkMax motorFL;
    private CANSparkMax motorFR;
    private CANSparkMax motorBL;
    private CANSparkMax motorBR;

    private double xAxisDeadband;
    private double yAxisDeadband;

    public DriveBase() {
      motorFR = new CANSparkMax(1, MotorType.kBrushless);
      motorFL = new CANSparkMax(4, MotorType.kBrushless);
      motorBR = new CANSparkMax(2, MotorType.kBrushless);
      motorBL = new CANSparkMax(3, MotorType.kBrushless);
      motorBR.setInverted(true);
      motorFR.setInverted(true);
      SmartDashboard.putNumber("FrontRight",getFrontRightEncoderValue());
    SmartDashboard.putNumber("BackRight", getBackRightEncoderValue());
    SmartDashboard.putNumber("FrontLeft",getFrontLeftEncoderValue());
    SmartDashboard.putNumber("BackLeft", getBackLeftEncoderValue());
    }

    
    public double deaden(double input, double deadband) {
        deadband = Math.min(1, deadband);
        deadband = Math.max(0, deadband);
       
        if (Math.abs(input) - deadband < 0) {
          return 0;
        }
        else {
          return Math.signum(input) * ((Math.abs(input) - deadband) / (1 - deadband));
        }
    }
      

    public void init() {

        motorFL.clearFaults();
        motorBL.clearFaults();
        motorFR.clearFaults();
        motorBR.clearFaults();
    }  //End of init method
  
  
  
  
  
    //Drive Command to send values to DriveBase Motors
    public void drive(double l, double r){
      motorFL.set(l);
      motorBL.set(l);
      motorFR.set(r);
      motorBR.set(r);
    }  //End of driveRobot
  
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
  }
  
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
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("FrontRight",getFrontRightEncoderValue());
    SmartDashboard.putNumber("BackRight", getBackRightEncoderValue());
    SmartDashboard.putNumber("FrontLeft",getFrontLeftEncoderValue());
    SmartDashboard.putNumber("BackLeft", getBackLeftEncoderValue());
  }
    
}

