package org.anotherteam.util;

import imgui.ImGui;

public final class ImGuiUtils {

    public static boolean imGuiHandle() {
        return ImGui.getIO().getWantCaptureKeyboard() || ImGui.getIO().getWantCaptureMouse();
    }
}
