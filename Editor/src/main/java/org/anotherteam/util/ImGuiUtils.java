package org.anotherteam.util;

import imgui.ImGui;

public final class ImGuiUtils {

    public static boolean imGuiWantKeyboard() {
        return ImGui.getIO().getWantCaptureKeyboard();
    }

    public static boolean imGuiWantMouse() {
        return ImGui.getIO().getWantCaptureMouse();
    }
}
