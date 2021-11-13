package org.anotherteam.editor.gui.window;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.jetbrains.annotations.NotNull;

public final class SaveLevelDialog extends DialogWindow {

    public SaveLevelDialog(@NotNull String levelName) {
        super(200, 50);
        val saveButton = new TextButton("Save", 0, 0, this);
        saveButton.setOnClick(() -> {
            Game.levelManager.saveLevel();
            Editor.closeWindow();
        });

        val text = new Label("Do you want save level?", 0, 0,this);
        text.setPos(0, height - text.getHeight());

        val levelText = new Label("To save: " + levelName, 0, 0,this);
        levelText.setPos(0, height - text.getHeight());
        levelText.setWidth(Math.max(levelText.getWidth(), text.getWidth()));
        text.setWidth(Math.max(levelText.getWidth(), text.getWidth()));
        width = levelText.getWidth();

        val closeButton = new TextButton("Close", 0, 0, this);
        closeButton.setPos(width - closeButton.getWidth(), 0);
        closeButton.setOnClick(Editor::closeWindow);
    }
}
