package com.qualcomm.robotcore.hardware;

public class Gamepad {

    public Gamepad() {

    }

    /**
     * left analog stick horizontal axis
     */
    public volatile float left_stick_x = 0f;

    /**
     * left analog stick vertical axis
     */
    public volatile float left_stick_y = 0f;

    /**
     * right analog stick horizontal axis
     */
    public volatile float right_stick_x = 0f;

    /**
     * right analog stick vertical axis
     */
    public volatile float right_stick_y = 0f;

    /**
     * dpad up
     */
    public volatile boolean dpad_up = false;

    /**
     * dpad down
     */
    public volatile boolean dpad_down = false;

    /**
     * dpad left
     */
    public volatile boolean dpad_left = false;

    /**
     * dpad right
     */
    public volatile boolean dpad_right = false;

    /**
     * button a
     */
    public volatile boolean a = false;

    /**
     * button b
     */
    public volatile boolean b = false;

    /**
     * button x
     */
    public volatile boolean x = false;

    /**
     * button y
     */
    public volatile boolean y = false;

    /**
     * button guide - often the large button in the middle of the controller. The OS may
     * capture this button before it is sent to the app; in which case you'll never
     * receive it.
     */
    public volatile boolean guide = false;

    /**
     * button start
     */
    public volatile boolean start = false;

    /**
     * button back
     */
    public volatile boolean back = false;

    /**
     * button left bumper
     */
    public volatile boolean left_bumper = false;

    /**
     * button right bumper
     */
    public volatile boolean right_bumper = false;

    /**
     * left stick button
     */
    public volatile boolean left_stick_button = false;

    /**
     * right stick button
     */
    public volatile boolean right_stick_button = false;

    /**
     * left trigger
     */
    public volatile float left_trigger = 0f;

    /**
     * right trigger
     */
    public volatile float right_trigger = 0f;
}
