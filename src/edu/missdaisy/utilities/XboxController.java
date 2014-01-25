package edu.missdaisy.utilities;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Creates an XboxController with predefined methods for finding the state/value
 * of Xbox controller buttons/axes
 * @author James
 */
public class XboxController extends Joystick {
    //Xbox Controller Button mapping
    final static int A_Button = 1,
            B_Button = 2,
            X_Button = 3,
            Y_Button = 4,
            Left_Bumper = 5,
            Right_Bumper = 6,
            Back_Button = 7,
            Start_Button = 8,
            Left_Stick = 9,
            Right_Stick = 10,
            
            //Xbox Axes mapping::
            Axis_LeftX = 1,
            Axis_LeftY = 2,
            Axis_Triggers = 3,
            Axis_RightX = 4,
            Axis_RightY = 5,
            //Not reccomended: buggy and unreliable
            Axis_DPad = 6;

    /**
     * Constructs a new XboxController with a specified port
     *
     * @param port
     */
    public XboxController(final int port) {
        super(port);
    }
    
    public boolean getStartButton() {
        return getRawButton(Start_Button);
    }
    
    public boolean getBackButton() {
        return getRawButton(Back_Button);
    }

    //Control Sticks::
    /**
     * returns the current state of the A Button
     */
    public boolean getAButton() {
        return getRawButton(A_Button);
    }

    /**
     * returns the current state of the B Button
     */
    public boolean getBButton() {
        return getRawButton(B_Button);
    }

    /**
     * returns the current state of the X_Button
     */
    public boolean getXButton() {
        return getRawButton(X_Button);
    }

    /**
     * returns the current state of the Y_Button
     */
    public boolean getYButton() {
        return getRawButton(Y_Button);
    }

    /**
     * returns the current state of the Left_Bumper
     */
    public boolean getLB() {
        return getRawButton(Left_Bumper);
    }

    /**
     * returns the current state of the Right_Bumper
     */
    public boolean getRB() {
        return getRawButton(Right_Bumper);
    }

    public boolean getRightTriggerHeld()
    {
        return getTriggerAxis() < -0.1;
    }

    public boolean getLeftTriggerHeld()
    {
        return getTriggerAxis() > 0.1;
    }

    /**
     * returns the current state of the Left_Stick
     */
    public boolean getLeftStickClick() {
        return getRawButton(Left_Stick);
    }

    /**
     * returns the current state of the Right_Stick
     */
    public boolean getRightStickClick() {
        return getRawButton(Right_Stick);
    }

    /**
     * returns the current value of the Axis_LeftX
     */
    public double getLeftXAxis() {
        return getRawAxis(Axis_LeftX);
    }

    /**
     * returns the current value of the Axis_LeftY
     */
    public double getLeftYAxis() {
        return getRawAxis(Axis_LeftY);
    }

    /**
     * returns the current value of the Axis_RightX
     */
    public double getRightXAxis() {
        return getRawAxis(Axis_RightX);
    }

    /**
     * returns the current value of the Axis_RightY
     */
    public double getRightYAxis() {
        return getRawAxis(Axis_RightY);
    }

    /**
     * returns the current value of the Axis_Triggers
     */
    public double getTriggerAxis() {
        return getRawAxis(Axis_Triggers);
    }

    /**
     * returns the current value of the Axis_DPad (NOTE:: Very buggy, not
     * recommended)
     */
    public double getDPadAxis() {
        return getRawAxis(Axis_LeftX);
    }
}
