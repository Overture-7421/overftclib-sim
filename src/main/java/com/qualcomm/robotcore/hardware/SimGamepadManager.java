package com.qualcomm.robotcore.hardware;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.*;

public class SimGamepadManager {
    public static class GamepadContainer {
        public GamepadContainer(Integer Id, String Name) {
            this.Id = Id;
            this.Name = Name;
        }
        public Gamepad Gamepad = new Gamepad();
        public Integer Id;
        public String Name;
    }
    final Lock lock = new ReentrantLock();
    private ArrayList<GamepadContainer> gamepads = new ArrayList<>();
    private final GLFWGamepadState gamepadState = GLFWGamepadState.create();
    /**
     *  Check if any controllers are connected before starting the program
     */
    public void Initialize() {
        for(int i = GLFW_JOYSTICK_1; i < GLFW_JOYSTICK_LAST; i++) {
            if(glfwJoystickIsGamepad(i) && glfwJoystickPresent(i)) {
                HandleJoystickConfigurationCallback(i, GLFW_CONNECTED);
            }
        }
    }
    public void HandleJoystickConfigurationCallback(int joystickId, int event) {
        lock.lock();
        if(event == GLFW_CONNECTED) {
            System.out.println("Connected joystick " + joystickId + " with name: " + glfwGetGamepadName(joystickId));
            gamepads.add(new GamepadContainer(joystickId, glfwGetGamepadName(joystickId)));
        }else if(event == GLFW_DISCONNECTED) {
            gamepads.removeIf((GamepadContainer container) -> container.Id == joystickId);
            System.out.println("Disconnected joystick " + joystickId);
        }
        lock.unlock();
    }

    public void PollControllers() {
        lock.lock();
        for (int i = 0; i < gamepads.size(); i++) {
            GamepadContainer container = gamepads.get(i);
            if (!glfwGetGamepadState(container.Id, gamepadState)) {
                continue;
            }

            Gamepad gamepad = container.Gamepad;

            gamepad.left_stick_x = gamepadState.axes(GLFW_GAMEPAD_AXIS_LEFT_X);
            gamepad.left_stick_y = gamepadState.axes(GLFW_GAMEPAD_AXIS_LEFT_Y);
            gamepad.right_stick_x = gamepadState.axes(GLFW_GAMEPAD_AXIS_RIGHT_X);
            gamepad.right_stick_y = gamepadState.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y);

            gamepad.left_trigger = gamepadState.axes(GLFW_GAMEPAD_AXIS_LEFT_TRIGGER);
            gamepad.right_trigger = gamepadState.axes(GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);

            gamepad.dpad_up = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW_PRESS;
            gamepad.dpad_down = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW_PRESS;
            gamepad.dpad_left = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW_PRESS;
            gamepad.dpad_right = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW_PRESS;

            gamepad.a = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
            gamepad.b = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_B) == GLFW_PRESS;
            gamepad.x = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_X) == GLFW_PRESS;
            gamepad.y = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_Y) == GLFW_PRESS;

            gamepad.guide = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_GUIDE) == GLFW_PRESS;
            gamepad.start = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_START) == GLFW_PRESS;
            gamepad.back = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_BACK) == GLFW_PRESS;

            gamepad.left_bumper = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) == GLFW_PRESS;
            gamepad.right_bumper = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == GLFW_PRESS;
            gamepad.left_stick_button = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_LEFT_THUMB) == GLFW_PRESS;
            gamepad.right_stick_button = gamepadState.buttons(GLFW_GAMEPAD_BUTTON_RIGHT_THUMB) == GLFW_PRESS;
        }
        lock.unlock();
    }
    public ArrayList<GamepadContainer> GetAllGamepads() {
        return gamepads;
    }

}
