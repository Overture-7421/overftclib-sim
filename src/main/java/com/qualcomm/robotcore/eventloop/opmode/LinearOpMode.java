package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

abstract public class LinearOpMode {
    public Gamepad gamepad1;
    public Gamepad gamepad2;

    public HardwareMap hardwareMap;

    private volatile boolean  userMethodReturned = false;
    private volatile boolean  userMonitoredForStart = false;
    private volatile boolean isStarted = false;
    volatile boolean stopRequested = false;
    private final Object runningNotifier = new Object();
    abstract public void runOpMode() throws InterruptedException;
    /**
     * Pauses until the play button has been pressed (or until the current thread
     * gets interrupted, which typically indicates that the OpMode has been stopped).
     */
    public void waitForStart() {
        while (!isStarted()) {
            synchronized (runningNotifier) {
                try {
                    runningNotifier.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public final boolean isStarted() {

        /*
         * What we're looking for here is that the user polled until the
         * the start condition was occurred.
         */
        if(isStarted) userMonitoredForStart = true;

        return this.isStarted || Thread.currentThread().isInterrupted();
    }

    public final boolean opModeIsActive() {
        boolean isActive = !this.isStopRequested() && this.isStarted();
        if (isActive) {
            idle();
        }
        return isActive;
    }

    public final void idle() {
        // Otherwise, yield back our thread scheduling quantum and give other threads at
        // our priority level a chance to run
        Thread.yield();
    }
    public final void requestOpModeStop() {
        stopRequested = true;
    }

    public final boolean isStopRequested() {
        return this.stopRequested || Thread.currentThread().isInterrupted();
    }

    final void internalStart() {
        stopRequested = false;
        isStarted = true;
        // It's important that the states are updated BEFORE LinearOpModes notify the runningNotifier
        internalOnStart();
    }

    // Package-private, called on the main event loop thread
    final void internalOnStart() {
        synchronized (runningNotifier) {
            runningNotifier.notifyAll();
        }
    }
}
