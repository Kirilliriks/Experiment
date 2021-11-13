package org.anotherteam.editor.level.selector;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.text.input.InputLabel;
import org.anotherteam.level.Level;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class LevelInspector extends Widget {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    private Level inspectedLevel;

    private final InputLabel nameInputLabel;

    public LevelInspector(float x, float y, int width, int height, GUIElement ownerElement) {
        super("Level inspector", x, y, width, height, ownerElement);
        flipTitle();
        color.set(DEFAULT_COLOR);
        nameInputLabel = new InputLabel("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
    }

    public void setLevel(@NotNull Level level) {
        inspectedLevel = level;
        nameInputLabel.setInputText(level.getName());
    }
}
