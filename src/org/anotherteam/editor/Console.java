package org.anotherteam.editor;

import imgui.ImGui;
import org.anotherteam.logger.GameLogger;

public final class Console {

    public static void imgui() {
        ImGui.begin("Console: " + GameLogger.STRINGS.size());

        for (final String string : GameLogger.STRINGS) {
            ImGui.text(string);
        }

        ImGui.end();
    }
}
