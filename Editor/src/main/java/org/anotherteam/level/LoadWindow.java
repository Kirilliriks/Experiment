package org.anotherteam.level;

import imgui.ImGui;
import org.anotherteam.game.Game;
import org.anotherteam.util.Popups;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.util.exception.LifeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class LoadWindow {

    private final List<String> levels;

    private  boolean opened = false;

    public LoadWindow() {
        this.levels = new ArrayList<>();
    }

    public void call() {
        if (opened) return;

        opened = true;

        ImGui.openPopup("####load");

        loadLevels();
    }

    public void update() {
        if (ImGui.beginPopupModal("Load level####load")) {

            ImGui.beginListBox("##level box");
            for (final String level : levels) {
                if (ImGui.selectable(level)) {
                    Game.LEVEL_MANAGER.load(level);

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

    private void close() {
        ImGui.closeCurrentPopup();

        opened = false;
        Popups.LOAD_LEVEL = false;
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
