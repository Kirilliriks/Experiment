package org.anotherteam.editor;

import imgui.ImGui;
import org.anotherteam.editor.widget.Widget;
import org.anotherteam.logger.GameLogger;

public final class Console extends Widget {

    public Console(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        onDirty();

        ImGui.begin("Console: " + GameLogger.STRINGS.size());

        for (final String string : GameLogger.STRINGS) {
            ImGui.text(string);
        }

        ImGui.end();
    }
}
