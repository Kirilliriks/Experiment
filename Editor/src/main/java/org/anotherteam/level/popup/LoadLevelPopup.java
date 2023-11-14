package org.anotherteam.level.popup;

import imgui.ImGui;
import org.anotherteam.Editor;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.util.exception.LifeException;
import org.anotherteam.widget.popup.Popup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class LoadLevelPopup extends Popup {

    private final List<String> levels = new ArrayList<>();

    public LoadLevelPopup() {
        super("Load level");
    }

    public void call() {
        super.call();

        loadLevels();
    }

    public void update() {
        if (ImGui.beginPopupModal("Load level####" + id)) {
            ImGui.beginListBox("##level box");
            for (final String level : levels) {
                if (ImGui.selectable(level)) {
                    Editor.getInstance().getGame().getLevelManager().load(level);

                    close();
                }
            }
            ImGui.endListBox();

            if (ImGui.button("Close")) {
                close();
            }

            ImGui.endPopup();
        }
    }

    private void loadLevels() {
        final var files = new File(AssetData.LEVELS_PATH).listFiles();
        if (files == null) {
            throw new LifeException("Room's atlases not found");
        }

        levels.clear();
        for (final var file : files) {
            levels.add(file.getName());
        }
    }
}
