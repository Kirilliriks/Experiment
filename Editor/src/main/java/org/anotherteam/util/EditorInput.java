package org.anotherteam.util;

import org.anotherteam.Input;

public final class EditorInput {

    public static boolean isKeyDown(Input.Key key) {
        if (ImGuiUtils.imGuiHandle()) {
            return false;
        }

        return Input.isKeyDown(key);
    }

    public static boolean isKeyPressed(Input.Key key) {
        if (ImGuiUtils.imGuiHandle()) {
            return false;
        }

        return Input.isKeyPressed(key);
    }

    public static boolean isAnyButtonPressed() {
        if (ImGuiUtils.imGuiHandle()) {
            return false;
        }

        return Input.isAnyButtonPressed();
    }

    public static boolean isAnyButtonDown() {
        if (ImGuiUtils.imGuiHandle()) {
            return false;
        }

        return Input.isAnyButtonDown();
    }

    public static boolean isButtonPressed(Input.MouseButton button) {
        return Input.isButtonPressed(button);
    }

    public static boolean isButtonDown(Input.MouseButton button) {
        return Input.isButtonDown(button);
    }
}
