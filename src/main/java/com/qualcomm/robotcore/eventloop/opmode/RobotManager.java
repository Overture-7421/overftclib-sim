package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.SimGamepadManager;

public class RobotManager {
    public enum RunState{
        Idle,
        WaitingTInitialize,
        Initialized,
        Running,
    }

    private Thread robotThread = null;
    private LinearOpMode opMode = null;
    private RunState state = RunState.Idle;

    public RunState GetCurrentState() {
        return state;
    }

    public void SetOpMode(Class<? extends LinearOpMode> opModeClass, HardwareMap hardwareMap)  {
        try{
            opMode = opModeClass.getConstructor().newInstance();
            opMode.gamepad1 = SimGamepadManager.GetGamepad(0);
            opMode.gamepad2 = SimGamepadManager.GetGamepad(1);
            opMode.hardwareMap = hardwareMap;
            state = RunState.Idle;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitializeOpMode() {
        state = RunState.Initialized;
        robotThread = new Thread(this::RobotLoop);
        robotThread.start();
    }

    public void StartOpMode() {
        opMode.internalStart();
        state = RunState.Running;
    }
    public void StopOpMode(){
        opMode.requestOpModeStop();

        if(robotThread.isAlive()) {
            try {
                robotThread.join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void RobotLoop() {
        if(opMode == null) {
            System.out.println("Started op mode that was not initialized");
            return;
        }

        try {
            opMode.runOpMode();
        } finally {
            state = RunState.Idle;
        }
    }
}
