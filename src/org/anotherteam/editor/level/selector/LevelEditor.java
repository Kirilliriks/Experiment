package org.anotherteam.editor.level.selector;

import lombok.val;
import org.anotherteam.Game;
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

    private static Level editableLevel;
    private String storedEditableLevel;

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
        createEmptyButton.setOnClick(LevelEditor::loadEmptyLevel);
        downButtons.addButton(createEmptyButton);

        saveLevelButton = new TextButton("Save editable level", 0, 0, downButtons);
        saveLevelButton.setOnClick(()-> LevelEditor.saveEditableLevel(true));
        downButtons.addButton(saveLevelButton);

        deleteLevelButton = new TextButton("Delete editable level", 0, 0, downButtons);
        deleteLevelButton.setOnClick(LevelEditor::deleteEditableLevel);
        downButtons.addButton(deleteLevelButton);
    }

    public void updateButtons(String currentLevelName) {
        val files = new File("levels/").listFiles();
        if (files == null) throw new LifeException("Level's not found");

        selector.clearChild();
        for (val file : files) {
            val btn = selector.addButton(FileUtils.getNameFromFile(file.getName()),
                    ()-> {
                        if (editableLevel != null) {
                            levelInspector.acceptChanges();
                            val saveWindow = new SaveLevelDialog(editableLevel.getName());
                            saveWindow.setOnAfterClose(() -> loadLevel(file.getName()));
                            Editor.callWindow(saveWindow);
                            return;
                        }
                        loadLevel(file.getName());
                    });

            if (file.getName().equals(currentLevelName + "." + Level.LEVEL_FILE_EXTENSION))
                selector.setHighlighted(btn);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }

    public void storeEditableLevel() {
        storedEditableLevel = FileUtils.LEVEL_GSON.toJson(Game.levelManager.getCurrentLevel());
    }

    public void restoreEditableLevel() {
        Game.levelManager.setLevel(FileUtils.LEVEL_GSON.fromJson(storedEditableLevel, Level.class));
    }

    public void loadLevel(String name) {
        editableLevel = Game.levelManager.loadLevel(name);
        levelInspector.setLevel(editableLevel);
        update();
    }

    public static void loadEmptyLevel() {
        editableLevel = Game.levelManager.setEmptyLevel();
        levelEditor.levelInspector.setLevel(editableLevel);
        saveEditableLevel(true);
    }

    public static void update() {
        levelEditor.updateButtons(editableLevel.getName());
    }

    @NotNull
    public static Level getEditableLevel() {
        return editableLevel;
    }

    public static void renameEditableLevel(String newName) {
        for (val btn : levelEditor.selector.getButtons()) {
            if (!btn.getLabelText().equals(editableLevel.getName())) continue;
            btn.setLabelText(newName);
        }

        FileUtils.renameLevel(newName, editableLevel);
        editableLevel.setName(newName);
        saveEditableLevel();
    }

    public static void saveEditableLevel() {
        saveEditableLevel(false);
    }

    public static void saveEditableLevel(boolean needUpdate) {
        FileUtils.saveEditableLevel(editableLevel);
        if (needUpdate) update();
    }

    public static void deleteEditableLevel() {
        FileUtils.deleteLevel(editableLevel);
        loadEmptyLevel();
        update();
    }
}
