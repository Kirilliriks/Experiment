package org.anotherteam.editor.level.selector;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.data.FileLoader;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.window.SaveLevelDialog;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.Level;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LevelSelector extends GUIElement {

    private final SwitchMenu selector;

    private final SwitchMenu downButtons;
    private final TextButton createEmptyButton;
    private final TextButton saveLevelButton;

    private Level editableLevel;
    private String storedEditableLevel;

    public LevelSelector(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        selector = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                width - Editor.DEFAULT_BORDER_SIZE * 2, height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        selector.setYInverted(true);

        downButtons = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);

        createEmptyButton = new TextButton("Create empty level", 0, 0, downButtons);
        createEmptyButton.setOnClick(()-> selector.addButton("Empty." + Level.LEVEL_FILE_EXTENSION, ()-> Game.levelManager.setEmptyLevel()));
        downButtons.addButton(createEmptyButton);

        saveLevelButton = new TextButton("Save editable level", 40, 0, downButtons);
        saveLevelButton.setOnClick(()-> Game.levelManager.saveLevel());
        downButtons.addButton(saveLevelButton);
    }

    public void fillButtons() {
        val files = new File("levels/").listFiles();
        if (files == null) throw new LifeException("Level's not found");

        for (val file : files) {
            val btn = selector.addButton(file.getName(),
                    ()-> {
                        if (editableLevel != null) {
                            val saveWindow = new SaveLevelDialog(editableLevel.getName());
                            saveWindow.setOnAfterClose(() -> loadLevel(file.getName()));
                            Editor.callWindow(saveWindow);
                            return;
                        }
                        loadLevel(file.getName());
                    });

            if (file.getName().equals(Game.levelManager.getCurrentLevel().getName() + "." + Level.LEVEL_FILE_EXTENSION))
                selector.setClicked(btn);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }

    public void loadLevel(String name) {
        editableLevel = Game.levelManager.loadLevel(name);
    }

    public void storeEditableLevel() {
        storedEditableLevel = FileLoader.LEVEL_GSON.toJson(Game.levelManager.getCurrentLevel());
    }

    public void restoreEditableLevel() {
        Game.levelManager.setLevel(FileLoader.LEVEL_GSON.fromJson(storedEditableLevel, Level.class));
    }

    @NotNull
    public Level getEditableLevel() {
        return editableLevel;
    }
}
