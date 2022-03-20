// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/** Add your docs here. */
public class DoubleButton extends Button {
    JoystickButton button1;
    JoystickButton button2;

    public DoubleButton(JoystickButton button1, JoystickButton button2){
        this.button1 = button1;
        this.button2 = button2;
    }

    @Override
    public boolean get(){
        return (button1.get() && button2.get());
    }
}
