package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.SimGamepadManager;
import org.overture.ftc.SimMotorManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RobotManager {
    public enum RunState{
        Idle,
        Initialized,
        Running,
    }

    private Thread robotThread = null;
    private LinearOpMode opMode = null;
    private Class<? extends LinearOpMode> opModeClass = null;
    private HardwareMap hardwareMap;
    private final ArrayList<SimGamepadManager.GamepadContainer> gamepads = new ArrayList<>();
    private RunState state = RunState.Idle;

    public RunState GetCurrentState() {
        return state;
    }
    private ScheduledExecutorService simLoop;
    public RobotManager() {
        try {
            simLoop = Executors.newSingleThreadScheduledExecutor();
            simLoop.scheduleAtFixedRate(this::SimLoop, 0, 20, TimeUnit.MILLISECONDS);

            gamepads.add(new SimGamepadManager.GamepadContainer(-1, ""));
            gamepads.add(new SimGamepadManager.GamepadContainer(-1, ""));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetGamepad(SimGamepadManager.GamepadContainer gamepad, int index){
        if(index < 0 || index > 2 || index >= gamepads.size()){
            return;
        }
        gamepads.set(index, gamepad);

        if(opMode != null) {
            opMode.gamepad1 = gamepads.get(0).Gamepad;
            opMode.gamepad2 = gamepads.get(1).Gamepad;
        }
    }

    public SimGamepadManager.GamepadContainer GetGamepad(int index) {
        if(index < 0 || index > 2 || index >= gamepads.size()){
            return new SimGamepadManager.GamepadContainer(-1, "");
        }

        return gamepads.get(index);
    }

    public void SetOpMode(Class<? extends LinearOpMode> opModeClass, HardwareMap hardwareMap)  {
        this.opModeClass =  opModeClass;
        this.hardwareMap = hardwareMap;
        state = RunState.Idle;
    }

    public void InitializeOpMode() {
        try{
            opMode = opModeClass.getConstructor().newInstance();
            opMode.gamepad1 = gamepads.get(0).Gamepad;
            opMode.gamepad2 = gamepads.get(1).Gamepad;
            opMode.hardwareMap = hardwareMap;

            state = RunState.Initialized;
            robotThread = new Thread(this::RobotLoop);
            robotThread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
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
            state = RunState.Idle;
            return;
        }

        try {
            opMode.runOpMode();
        } finally {
            state = RunState.Idle;
        }
    }

    private void SimLoop() {
        SimMotorManager.UpdateSimMotors();
    }
}
