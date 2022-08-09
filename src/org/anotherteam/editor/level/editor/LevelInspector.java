package org.anotherteam.editor.level.editor;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.text.input.InputPart;
import org.anotherteam.level.Level;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class LevelInspector extends Widget {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    private Level inspectedLevel;

    private final InputPart nameInputLabel;

    public LevelInspector(float x, float y, int width, int height, GUIElement ownerElement) {
        super("Level inspector", x, y, width, height, ownerElement);
        flipTitle();
        color.set(DEFAULT_COLOR);
        nameInputLabel = new InputPart("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(()-> Editor.LEVEL_EDITOR.renameLevel(nameInputLabel.getValue()));
    }

    public void acceptChanges() {
        final String newName = nameInputLabel.getValue();
        if (inspectedLevel.getName().equals(newName)) return;

        Editor.LEVEL_EDITOR.renameLevel(nameInputLabel.getValue());
    }

    public void setLevel(@NotNull Level level) {
        inspectedLevel = level;
        nameInputLabel.setValue(level.getName());
    }
}
