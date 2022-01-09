package org.anotherteam.editor.level.editor;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.level.room.RoomEditor;
import org.anotherteam.util.FileUtils;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.window.SaveLevelDialog;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.Level;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class LevelEditor extends GUIElement {

    private static LevelEditor levelEditor;

    private final SwitchMenu selector;
    private final LevelInspector levelInspector;

    private final SwitchMenu downButtons;
    private final TextButton createEmptyButton;
    private final TextButton saveLevelButton;
    private final TextButton deleteLevelButton;

    private static Level level;
    private String storedEditedLevel;

    public LevelEditor(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        levelEditor = this;

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        selector = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                (int)(width * 0.65f), height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        selector.setInvertedY(true);

        levelInspector = new LevelInspector(selector.getWidth() + Editor.DEFAULT_BORDER_SIZE * 2, -Editor.DEFAULT_BORDER_SIZE,
                width - selector.getWidth() - Editor.DEFAULT_BORDER_SIZE * 3, selector.getHeight(), this);
        levelInspector.setInvertedY(true);

        downButtons = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);

        createEmptyButton = new TextButton("Create empty level", 0, 0, downButtons);
        createEmptyButton.setOnClick(this::createAndLoadEmptyLevel);
        downButtons.addButton(createEmptyButton);

        saveLevelButton = new TextButton("Save editable level", 0, 0, downButtons);
        saveLevelButton.setOnClick(this::saveLevel);
        downButtons.addButton(saveLevelButton);

        deleteLevelButton = new TextButton("Delete editable level", 0, 0, downButtons);
        deleteLevelButton.setOnClick(this::deleteLevel);
        downButtons.addButton(deleteLevelButton);
    }

    public void updateButtons(String currentLevelName) {
        val files = new File("levels/").listFiles();
        if (files == null) throw new LifeException("Level's not found");

        selector.clearChild();
        for (val file : files) {
            val btn = selector.addButton(FileUtils.getNameFromFile(file.getName()),
                    ()-> {
                        if (level != null) {
                            levelInspector.acceptChanges();
                            val saveWindow = new SaveLevelDialog(level.getName());
                            saveWindow.setOnAfterClose(() -> loadLevel(file.getName()));
                            Editor.callWindow(saveWindow);
                            return;
                        }
                        loadLevel(file.getName());
                    });

            if (file.getName().equals(currentLevelName + "." + Level.LEVEL_FILE_EXTENSION)) {
                selector.setHighlighted(btn);
            }
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }

    private void updateEditor() {
        levelEditor.updateButtons(level.getName());
    }

    public void storeLevel() {
        storedEditedLevel = FileUtils.LEVEL_GSON.toJson(Game.levelManager.getCurrentLevel());
    }

    public void restoreLevel() {
        level = Game.levelManager.setLevel(FileUtils.LEVEL_GSON.fromJson(storedEditedLevel, Level.class));
        Editor.roomEditor.resetRoom();
        levelInspector.setLevel(level);
        updateEditor();
    }

    public void loadLevel(String name) {
        level = Game.levelManager.loadLevel(name);
        levelInspector.setLevel(level);
        updateEditor();
    }

    public void createAndLoadEmptyLevel() {
        level = Game.levelManager.setEmptyLevel();
        levelEditor.levelInspector.setLevel(level);
        saveLevel(true);
    }

    public void renameLevel(String newName) {
        for (val btn : levelEditor.selector.getButtons()) {
            if (!btn.getLabelText().equals(level.getName())) continue;
            btn.setLabelText(newName);
        }

        FileUtils.renameLevel(newName, level);
        level.setName(newName);
        saveLevel();
    }

    public void saveLevel() {
        saveLevel(false);
    }

    public void saveLevel(boolean needUpdate) {
        FileUtils.saveEditableLevel(level);
        if (needUpdate) updateEditor();
    }

    public void deleteLevel() {
        FileUtils.deleteLevel(level);
        createAndLoadEmptyLevel();
        updateEditor();
    }

    @NotNull
    public Level getLevel() {
        return level;
    }
}
