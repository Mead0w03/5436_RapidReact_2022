package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {

    private NetworkTable visionLimelight;

    private NetworkTableEntry tx; //horizontal offset
    private NetworkTableEntry ty; //vertical offset

    private double hError; //current horizontal offset 
    private double vError; //current vertical offset

    private double goodRange;

    private DriveBase driveTrain;

    public Vision(NetworkTable limelight, DriveBase driveTrain) {
        visionLimelight = limelight;
        tx = visionLimelight.getEntry("tx");
        ty = visionLimelight.getEntry("ty");
        hError = 0.0;
        vError = 0.0;

        this.driveTrain = driveTrain;

    }

    public void background() {
        hError = tx.getDouble(0.0);
        vError = ty.getDouble(0.0);
    } //updates horizontal error the entire time the robot is enable, communcates with network table

    public boolean alignX(double offset) {
        boolean alignStatus = false;

        double currentError = Math.abs(offset);

        currentError = Math.max(currentError, goodRange);

        double speed = Math.min(.02 * hError, 1);

        if(offset < 0) {
            alignStatus = false; //true is placeholder
            driveTrain.drive(-speed, -speed);
        } else if(offset > 0) {
            alignStatus = false;
            driveTrain.drive(speed, speed);
            
        }
        if (currentError == 1) {
            alignStatus = true;
            driveTrain.stopAllDrivetrainMotors();
        }


        return alignStatus;
    }

    //change angle of shooter based on verticle offset
    public boolean alignY(double offset) {
        boolean alignStatus = false;
        double speed;

        double currentError = Math.abs(offset);
        currentError = Math.max(currentError, .1);
        speed = Math.min(.09 * currentError, 1);

        if(offset > 0) {
            alignStatus = false;
            driveTrain.drive(speed, -speed);

        } else if(offset < 0) {
            alignStatus = false;
            driveTrain.drive(-speed, speed);
            
        }

        if (currentError == 1) {
            alignStatus = true;
            driveTrain.stopAllDrivetrainMotors();
        }

        return alignStatus;
    }




    

}
