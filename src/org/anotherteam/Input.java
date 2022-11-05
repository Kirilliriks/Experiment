package org.anotherteam;

import imgui.ImGuiIO;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.CharUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;

import java.util.HashMap;
import java.util.Map;

public final class Input {

    public static ImGuiIO imGuiIO = null;
    private final static Map<Integer, MouseButton> buttons = new HashMap<>();
    private final static Vector2f mousePos = new Vector2f(0, 0);
    private final static Vector2f lastMousePos = new Vector2f(0, 0);

    private static Key lastPrintedKey = null;
    public static Map<Integer, Key> keys = new HashMap<>();

    private static float mouseWheelVelocity = 0.0f;

    public final static Key KEY_W = new Key(GLFW.GLFW_KEY_W);
    public final static Key KEY_A = new Key(GLFW.GLFW_KEY_A);
    public final static Key KEY_S = new Key(GLFW.GLFW_KEY_S);
    public final static Key KEY_D = new Key(GLFW.GLFW_KEY_D);
    public final static Key KEY_E = new Key(GLFW.GLFW_KEY_E);

    public final static Key KEY_TILDA = new Key(GLFW.GLFW_KEY_GRAVE_ACCENT);

    public final static Key KEY_ESCAPE = new Key(GLFW.GLFW_KEY_ESCAPE);
    public final static Key KEY_ENTER = new Key(GLFW.GLFW_KEY_ENTER);
    public final static Key KEY_SPACE = new Key(GLFW.GLFW_KEY_SPACE);
    public final static Key KEY_SHIFT = new Key(GLFW.GLFW_KEY_LEFT_SHIFT);
    public final static Key KEY_BACKSPACE = new Key(GLFW.GLFW_KEY_BACKSPACE);

    public final static MouseButton MOUSE_LEFT_BUTTON = new MouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public final static MouseButton MOUSE_RIGHT_BUTTON = new MouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback mouseMove;
    private final GLFWMouseButtonCallback mouseButton;
    private final GLFWScrollCallback mouseScroll;

    public static boolean isAnyKeyDown() {
        if (isImGuiHandleKeyboard()) return false;

        if (lastPrintedKey == null) return false;
        return lastPrintedKey.down;
    }

    public static boolean isAnyKeyPressed() {
        if (isImGuiHandleKeyboard()) return false;

        if (lastPrintedKey == null) return false;
        return lastPrintedKey.pressed;
    }

    @Nullable
    public static Key getLastPrintedKey() {
        return lastPrintedKey;
    }

    public static boolean isKeyDown(Key key) {
        if (isImGuiHandleKeyboard()) return false;

        if (key.isLetter()) {
            final int anotherChar = CharUtil.toAnotherCase(key.keyCode);

            final Key anotherKey = keys.get(anotherChar);
            if (anotherKey != null && anotherKey.down) return true;
        }
        return key.down;
    }

    public static boolean isKeyPressed(Key key) {
        if (isImGuiHandleKeyboard()) return false;

        if (key.isLetter()) {
            final int anotherChar = CharUtil.toAnotherCase(key.keyCode);

            final Key anotherKey = keys.get(anotherChar);
            if (anotherKey != null && anotherKey.pressed) return true;
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

    public static float getLastMouseX() {
        return lastMousePos.x;
    }

    public static float getLastMouseY() {
        return lastMousePos.y;
    }

    @NotNull
    public static Vector2f getLastMousePos() {
        return lastMousePos;
    }

    public static float getMouseWheelVelocity() {
        return mouseWheelVelocity;
    }

    public static boolean isImGuiHandle() {
        return isImGuiHandleKeyboard() || isImGuiHandleMouse();
    }

    public static boolean isImGuiHandleKeyboard() {
        return imGuiIO != null && (imGuiIO.getWantCaptureKeyboard() || imGuiIO.getWantTextInput());
    }

    public static boolean isImGuiHandleMouse() {
        return imGuiIO != null && (imGuiIO.getWantSetMousePos() || imGuiIO.getWantCaptureMouse());
    }

    public Input(@NotNull Window ownerWindow) {

        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                key = CharUtil.transformKeyCode(key, mods);

                if (!keys.containsKey(key)) {
                    keys.put(key, new Key(key));
                }

                final var bool = action != GLFW.GLFW_RELEASE;
                lastPrintedKey = keys.get(key);
                lastPrintedKey.toggle(bool);

                if (CharUtil.isPrintable(key)) {
                    final int anotherKey = CharUtil.toAnotherCase(key);
                    if (keys.containsKey(anotherKey)) keys.get(anotherKey).toggle(bool);
                }
            }
        };
        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                lastMousePos.set(mousePos);
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

        mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke (long win, double dx, double dy) {
                mouseWheelVelocity = (float) dy;
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

    @NotNull
    public GLFWScrollCallback getMouseScroll() {
        return mouseScroll;
    }

    public void tick() {
        for (final var key : keys.values()) {
            key.tick();
        }

        for (final var button : buttons.values()) {
            button.tick();
        }

        lastPrintedKey = null;

        mouseWheelVelocity = 0.0f;
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButton.free();
        mouseScroll.free();
    }

    public static class MouseButton {
        private final int buttonCode;

        private boolean down = false;
        private boolean pressed = false;
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

        public boolean isPressed() {
            return isButtonPressed(this);
        }

        public boolean isDown() {
            return isButtonDown(this);
        }

        public boolean isWasDown() {
            return wasDown;
        }
    }

    public static class Key {
        private final int keyCode;

        private boolean down = false;
        private boolean pressed = false;
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

        public boolean isPressed() {
            return isKeyPressed(this);
        }

        public boolean isDown() {
            return isKeyDown(this);
        }

        public boolean isWasDown() {
            return wasDown;
        }
    }
}
