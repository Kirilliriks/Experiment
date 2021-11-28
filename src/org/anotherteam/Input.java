package org.anotherteam;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.CharUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.HashMap;
import java.util.Map;

public final class Input {
    private final static Map<Integer, MouseButton> buttons = new HashMap<>();
    private final static Vector2f mousePos = new Vector2f(0, 0);

    private static Key lastPrintedKey;
    public static Map<Integer, Key> keys = new HashMap<>();

    public final static Key KEY_W = new Key(GLFW.GLFW_KEY_W);
    public final static Key KEY_A = new Key(GLFW.GLFW_KEY_A);
    public final static Key KEY_S = new Key(GLFW.GLFW_KEY_S);
    public final static Key KEY_D = new Key(GLFW.GLFW_KEY_D);
    public final static Key KEY_E = new Key(GLFW.GLFW_KEY_E);

    public final static Key KEY_TILDA = new Key(GLFW.GLFW_KEY_GRAVE_ACCENT);

    public final static Key KEY_ESCAPE = new Key(GLFW.GLFW_KEY_ESCAPE);
    public final static Key KEY_SPACE = new Key(GLFW.GLFW_KEY_SPACE);
    public final static Key KEY_SHIFT = new Key(GLFW.GLFW_KEY_LEFT_SHIFT);
    public final static Key KEY_BACKSPACE = new Key(GLFW.GLFW_KEY_BACKSPACE);

    public final static MouseButton MOUSE_LEFT_BUTTON = new MouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public final static MouseButton MOUSE_RIGHT_BUTTON = new MouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback mouseMove;
    private final GLFWMouseButtonCallback mouseButton;

    public static boolean isAnyKeyDown() {
        if (lastPrintedKey == null) return false;
        return lastPrintedKey.down;
    }

    public static boolean isAnyKeyPressed() {
        if (lastPrintedKey == null) return false;
        return lastPrintedKey.pressed;
    }

    @Nullable
    public static Key getLastPrintedKey() {
        return lastPrintedKey;
    }

    public static boolean isKeyDown(Key key) {
        if (key.isLetter()) {
            val anotherChar = (int)CharUtil.toAnotherCase(key.keyCode);
            if (keys.containsKey(anotherChar) && keys.get(anotherChar).down) return true;
        }
        return key.down;
    }

    public static boolean isKeyPressed(Key key) {
        if (key.isLetter()) {
            val anotherChar = (int)CharUtil.toAnotherCase(key.keyCode);
            if (keys.containsKey(anotherChar) && keys.get(anotherChar).pressed) return true;
        }
        return key.pressed;
    }

    public static boolean isAnyButtonPressed() {
        return MOUSE_LEFT_BUTTON.pressed || MOUSE_RIGHT_BUTTON.pressed;
    }

    public static boolean isAnyButtonDown() {
        return MOUSE_LEFT_BUTTON.down || MOUSE_RIGHT_BUTTON.down;
    }

    public static boolean isButtonPressed(MouseButton button) {
        return button.pressed;
    }

    public static boolean isButtonDown(MouseButton button) {
        return button.down;
    }

    public static float getMouseX() {
        return mousePos.x;
    }

    public static float getMouseY() {
        return mousePos.y;
    }

    @NotNull
    public static Vector2f getMousePos() {
        return mousePos;
    }

    public Input(@NotNull Window ownerWindow) {
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                key = CharUtil.transformKeyCode(key, mods);
                val anotherKey = (int)CharUtil.toAnotherCase(key);

                if (!keys.containsKey(key)) {
                    keys.put(key, new Key(key));
                }

                val bool = action != GLFW.GLFW_RELEASE;
                lastPrintedKey = keys.get(key);
                lastPrintedKey.toggle(bool);
                if (keys.containsKey(anotherKey)) keys.get(anotherKey).toggle(bool);
            }
        };
        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mousePos.set((float)xpos,  (float) (ownerWindow.getHeight() - ypos));
            }
        };
        mouseButton = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (!buttons.containsKey(button)) return;

                buttons.get(button).toggle(action != GLFW.GLFW_RELEASE);
            }
        };
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
        for (val key : keys.values()) {
            key.tick();
        }
        for (val button : buttons.values()) {
            button.tick();
        }
        lastPrintedKey = null;
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButton.free();
    }

    public static class MouseButton {
        public int buttonCode;
        public boolean down = false, pressed = false;
        private boolean wasDown = false;

        public MouseButton(int code) {
            buttonCode = code;
            buttons.put(code, this);
        }

        public void toggle(boolean bool) {
            down = bool;
        }

        public void tick() {
            pressed = !wasDown && down;
            wasDown = down;
        }
    }

    public static class Key {
        public int keyCode;
        public boolean down = false, pressed = false;
        private boolean wasDown = false;

        public Key(int code) {
            keyCode = code;
            keys.put(code, this);
        }

        public void toggle(boolean bool) {
            down = bool;
        }

        public void tick() {
            pressed = !wasDown && down;
            wasDown = down;
        }

        public char getChar() {
            return (char) keyCode;
        }

        public boolean isLetter() {
            return CharUtil.isLetter(keyCode);
        }

        public boolean isPrintable() {
            return CharUtil.isPrintable(keyCode);
        }

        public boolean isLetterOrDigit() {
            return CharUtil.isLetterOrDigit(keyCode);
        }
    }
}
