package com.qualcomm.robotcore.hardware;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class SimGamepadManager {
    static private final HashMap<Integer, Gamepad> gamepads = new HashMap<>(2);
    static private final int[] gamepadIndeces = {0, 1};
    static private final GLFWGamepadState gamepadState = GLFWGamepadState.create();

    /**
     *  Check if any controllers are connected before starting the program
     */
    static public void Initialize() {
        for(int i = GLFW_JOYSTICK_1; i < GLFW_JOYSTICK_LAST; i++) {
            if(glfwJoystickIsGamepad(i) && glfwJoystickPresent(i)) {
                HandleJoystickConfigurationCallback(i, GLFW_CONNECTED);
            }
        }
    }
    static public void HandleJoystickConfigurationCallback(int joystickId, int event) {
        synchronized (gamepads) {
            System.out.println("Configuration callback called!");
            if(event == GLFW_CONNECTED) {
                System.out.println("Connected joystick " + joystickId + " with name: " + glfwGetGamepadName(joystickId));
                gamepads.put(joystickId, new Gamepad());
            }else if(event == GLFW_DISCONNECTED) {
                System.out.println("Disconnected joystick " + joystickId + " with name: " + glfwGetGamepadName(joystickId));
            }
        }
    }

    static public void PollControllers() {
        synchronized (gamepads) {
            gamepads.forEach((Integer id, Gamepad gamepad) -> {
                if(!glfwGetGamepadState(id, gamepadState)) {
                    return;
                }

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
            });
        }
    }

    static public Gamepad GetGamepad(int index) {
        synchronized (gamepads) {
            int gamepadIndex = gamepadIndeces[index];
            if(!gamepads.containsKey(gamepadIndex)) {
                System.out.println("Tried to get gamepad with index " + gamepadIndex + " but it did not exist...");
                return new Gamepad();
            }
            return gamepads.get(gamepadIndex);

        }
    }

    static public void SetGamepadIndex(int gamepad, int index) {
        synchronized (gamepads){
            if(gamepad > 2 || gamepad < 0) {
                throw new IllegalArgumentException("Can only use two gamepads");
            }
            gamepadIndeces[gamepad] = index;
        }
    }
}
