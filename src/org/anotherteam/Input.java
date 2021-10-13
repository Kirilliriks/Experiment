package org.anotherteam;

import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.HashMap;
import java.util.Map;

public final class Input {
    private final static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private final static Vector2f mousePos = new Vector2f(0, 0);

    public static Map<Integer, Key> keys = new HashMap<>();

    public final static Key KEY_W = new Key(GLFW.GLFW_KEY_W);
    public final static Key KEY_A = new Key(GLFW.GLFW_KEY_A);
    public final static Key KEY_S = new Key(GLFW.GLFW_KEY_S);
    public final static Key KEY_D = new Key(GLFW.GLFW_KEY_D);
    public final static Key KEY_E = new Key(GLFW.GLFW_KEY_E);

    public final static Key KEY_ESCAPE = new Key(GLFW.GLFW_KEY_ESCAPE);
    public final static Key KEY_SPACE = new Key(GLFW.GLFW_KEY_SPACE);
    public final static Key KEY_SHIFT = new Key(GLFW.GLFW_KEY_LEFT_SHIFT);

    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback mouseMove;
    private final GLFWMouseButtonCallback mouseButton;

    public static boolean isKeyDown(Key key) {
        return key.down;
    }

    public static boolean isKeyPressed(Key key) {
        return key.pressed;
    }

    public static boolean isButtonDown(int key) {
        return buttons[key];
    }

    @NotNull
    public static Vector2f getMousePos() {
        return mousePos;
    }

    private final Window ownerWindow;

    public Input(@NotNull Window ownerWindow) {
        this.ownerWindow = ownerWindow;
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (!keys.containsKey(key))
                    return;

                keys.get(key).toggle(action != GLFW.GLFW_RELEASE);
            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mousePos.set((float)xpos,  ownerWindow.getHeight() - (float)ypos);
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

    public void tick() {
        for (Key key : keys.values()) {
            key.tick();
        }
    }

    public static class Key {

        public int keycode;
        public boolean down = false, pressed = false;
        private boolean wasDown = false;

        public Key(int code) {
            keycode = code;
            keys.put(code, this);
        }

        public void toggle(boolean bool) {
            down = bool;
        }

        public void tick() {
            pressed = !wasDown && down;
            wasDown = down;
        }
    }
}
