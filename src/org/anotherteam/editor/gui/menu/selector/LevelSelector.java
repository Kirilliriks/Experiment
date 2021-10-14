package org.anotherteam.editor.gui.menu.selector;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.util.Color;

public class LevelSelector extends GUIElement {

    public LevelSelector(float x, float y) {
        super(x, y, Color.WHITE);
        val editor = Editor.getInstance();
        width = editor.getWidth() - editor.getWidth() / 4;
        height = -editor.getHeight() + editor.getDownBorder();
    }
}
