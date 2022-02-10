package frc.robot.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class LeftTrigger extends Trigger {
    private XboxController xboxController = new XboxController(0);
    
    public boolean get() {
      // This returns whether the trigger is active
      return xboxController.getRawAxis(XboxController.Axis.kLeftTrigger.value) > 0.3;
    }

  } 
