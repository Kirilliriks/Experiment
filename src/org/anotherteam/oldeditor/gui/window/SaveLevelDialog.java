package org.anotherteam.oldeditor.gui.window;

import org.anotherteam.Input;
import org.anotherteam.oldeditor.Editor;
import org.anotherteam.oldeditor.gui.text.Label;
import org.anotherteam.oldeditor.gui.menu.text.TextButton;
import org.anotherteam.oldeditor.level.editor.LevelEditor;
import org.jetbrains.annotations.NotNull;

public final class SaveLevelDialog extends DialogWindow {

    public SaveLevelDialog(@NotNull String levelName) {
        super(200, 50);
        final var saveButton = new TextButton("Save", 0, 0, this);
        saveButton.setOnClick((info) -> {
            LevelEditor.editor().saveLevel();
            Editor.closeWindow();
        });

        final var text = new Label("Do you want save level?", 0, 0,this);
        text.setPos(0, height - text.getHeight());

        final var levelText = new Label("Level: " + levelName, 0, 0,this);
        levelText.setPos(0, height - text.getHeight() - levelText.getHeight());
        levelText.setWidth(Math.max(levelText.getWidth(), text.getWidth()));
        text.setWidth(Math.max(levelText.getWidth(), text.getWidth()));

        width = levelText.getWidth();

        final var closeButton = new TextButton("Close", 0, 0, this);
        closeButton.setPos(width - closeButton.getWidth(), 0);
        closeButton.setOnClick((info) -> Editor.closeWindow());
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (Input.isKeyPressed(Input.KEY_ESCAPE)) {
            Editor.closeWindow();
        }
    }
}
