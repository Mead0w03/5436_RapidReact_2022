// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public class CanBusConfig{
        // Drive Base
        public static final int FRONT_RIGHT = 1; 
        public static final int FRONT_LEFT = 3; 
        public static final int BACK_RIGHT = 2; 
        public static final int BACK_LEFT = 4; 
        
        //public static final double deadband = .1;

        // Shooter
        public static final int LAUNCHER = 5;
        public static final int FEEDER = 6;

        //public static final double highRPM = 1234; //update
        //public static final double lowRPM = 1234; //update
        //public static final double HkP = .001; //update
        //public static final double HkI = 0.0;
        //public static final double HkD = 0.0;
        //public static final double LkP = .001; //update
        //public static final double LkI = 0.0;
        //public static final double LkD = 0.0;

        // Intake
        public static final int CARGO = 7;
        public static final int RETRACT_LEFT = 8;
        public static final int RETRACT_RIGHT = 9;
        public static final int STORAGE = 15;

        // Climber
        public static final int INNER_ARM = 10;
        public static final int OUTER_ARM = 11;
        public static final int TILT = 12;
        public static final int SOLENOID = 14;

        // Hardware
        public static final int PDP = 50;

        // Sensors

        //Gyro
        public static final int GYRO = 40;
        

    }

    public class ClimberConfig{
        public static final double TARGET_MOVEMENT = 18500;
        public static final double FULLY_DESCENDED = -193800;  //205000
        public static final double FULLY_ASCENDED = 78000;
        public static final double OUTER_FULLY_EXTENDED = 350000; //355000
        public static final double OUTER_FULLY_RETRACTED = 7500;
        public static final double OUTER_LEGAL_LIMIT = 275000;
    }

    public class IntakeConfig{
        public static final double RETRACT_ARM_DOWN_POSITION = -5.4;
    }
}
