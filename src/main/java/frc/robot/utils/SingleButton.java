// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/** Add your docs here. */
public class SingleButton extends Button {
    JoystickButton include;
    JoystickButton exclude;

    public SingleButton(JoystickButton include, JoystickButton exclude){
        this.include = include;
        this.exclude = exclude;
    }

    @Override
    public boolean get(){
        return (include.get() && !exclude.get());
    }
}
