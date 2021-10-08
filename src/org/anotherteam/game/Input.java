package org.anotherteam.game;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public final class Input {
    private final static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private final static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;

    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback mouseMove;
    private final GLFWMouseButtonCallback mouseButton;

    public Input() {
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButton = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButton.free();
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isButtonDown(int key) {
        return buttons[key];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    @NotNull
    public GLFWKeyCallback getKeyboard() {
        return keyboard;
    }

    @NotNull
    public GLFWCursorPosCallback getMouseMove() {
        return mouseMove;
    }

    @NotNull
    public GLFWMouseButtonCallback getMouseButton() {
        return mouseButton;
    }
}
