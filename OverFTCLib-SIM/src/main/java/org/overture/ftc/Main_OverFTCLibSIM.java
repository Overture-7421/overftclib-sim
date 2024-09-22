package org.overture.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.RobotManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.SimGamepadManager;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImString;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Main_OverFTCLibSIM extends Application{
    private final OpModeManager opModeManager = new OpModeManager();
    private final RobotManager robotManager = new RobotManager();
    String[] modes = {"Teleop", "Auto"};
    ArrayList<OpModeManager.OpModeEntry> opModes = new ArrayList<>();
    int selectedModeIndex = 0;
    int selectedOpModeIndex = 0;

    String[] robotStateModes = {"Initialize", "Start"};
    int selectedRobotStateIndex = 0;

    ImString robotNameString = new ImString();
    Class<? extends LinearOpMode> selectedOpMode = null;

    public void opModeWindow(){
        ImGui.begin("OpMode");

        if(robotManager.GetCurrentState() != RobotManager.RunState.Idle) {
            ImGui.text("Currently running OpMode \"" + opModes.get(selectedOpModeIndex).Name + "\"");
            ImGui.end();
            return;
        }

        ImGui.text("Select Mode");
        if(ImGui.beginListBox("Listbox1", new ImVec2(-Float.MIN_VALUE, 5 * ImGui.getTextLineHeightWithSpacing()))){
            for(int i = 0; i < modes.length; i++) {
                final boolean isSelected = selectedModeIndex == i;
                if(ImGui.selectable(modes[i], isSelected)) {
                    selectedModeIndex = i;
                }
                if(isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endListBox();
        }

        ImGui.text("Select Op Mode");
        if(ImGui.beginListBox("Listbox2", new ImVec2(-Float.MIN_VALUE, 5 * ImGui.getTextLineHeightWithSpacing()))){
            opModes = selectedModeIndex == 0 ? opModeManager.GetTeleopOpModes() : opModeManager.GetAutoOpModes();

            for(int i = 0; i < opModes.size(); i++) {
                final boolean isSelected = selectedOpModeIndex == i;
                if(ImGui.selectable(opModes.get(i).Name, isSelected)) {
                    selectedOpModeIndex = i;

                    if(selectedOpMode != opModes.get(i).Class) {
                        selectedOpMode = opModes.get(i).Class;
                        robotManager.SetOpMode(selectedOpMode, new HardwareMap(robotNameString.get()));
                    }
                }
                if(isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endListBox();
        }

        ImGui.end();
    }

    public void robotControlWindow() {
        ImGui.begin("Driver Station");
        ImGui.text("Simulated Robot Name");
        ImGui.inputText("Name", robotNameString);

        if(selectedOpMode == null) {
            ImGui.text("Select an Op Mode first...");
            ImGui.end();
            return;
        }

        if(robotManager.GetCurrentState() == RobotManager.RunState.Idle){
            if(ImGui.button("Initialize")) {
                robotManager.InitializeOpMode();
            }
            ImGui.end();
            return;
        }

        if(robotManager.GetCurrentState() == RobotManager.RunState.Initialized) {
            if(ImGui.button("Start")) {
                robotManager.StartOpMode();
            }
            ImGui.end();
            return;
        }

        if(ImGui.button("Stop")){
            robotManager.StopOpMode();
            selectedOpMode = null;
            ImGui.end();
            return;
        }

        ImGui.end();
    }

    @Override
    public void preRun() {
        SimGamepadManager.Initialize();
        glfwSetJoystickCallback(SimGamepadManager::HandleJoystickConfigurationCallback);
    }
    @Override
    public void configure(final Configuration config) {
        config.setTitle("OverFTC Sim");
    }
    @Override
    public void process() {
        SimGamepadManager.PollControllers();
        SimMotorManager.UpdateSimMotors(); //Move to a thread not related to UI
        opModeWindow();
        robotControlWindow();
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CombinedRuntimeLoader.loadLibraries(Main_OverFTCLibSIM.class, "wpiutiljni", "wpimathjni", "ntcorejni");
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.startServer();

        launch(new Main_OverFTCLibSIM());
    }
}