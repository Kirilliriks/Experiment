package org.anotherteam.level.popup;

import imgui.ImGui;
import imgui.type.ImString;
import org.anotherteam.Editor;
import org.anotherteam.game.level.Level;
import org.anotherteam.util.FileUtils;
import org.anotherteam.widget.popup.Popup;

public final class RenameLevelPopup extends Popup {

    private final ImString name = new ImString();

    public RenameLevelPopup() {
        super("Rename level");
    }

    @Override
    public void update() {
        if (ImGui.beginPopupModal("Rename level####" + id)) {

            ImGui.inputText("Name", name);

            if (ImGui.button("Save")) {
                final Level level = Editor.getInstance().getGame().getLevelManager().getLevel();
                final String lastName = level.getName();

                level.setName(name.get());

                Editor.saveLevel();
                try {
                    FileUtils.deleteLevel(lastName);
                } catch (Throwable ignore) { }

                close();
            }

            if (ImGui.button("Close")) {
                close();
            }

            ImGui.endPopup();
        }
    }
}
