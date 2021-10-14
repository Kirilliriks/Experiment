package org.anotherteam.editor.gui.menu.selector;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.util.Color;

public class LevelSelector extends GUIElement {

    private final SwitchMenu selector;

    public LevelSelector(float x, float y) {
        super(x, y, new Color(150, 150, 150, 255));
        val editor = Editor.getInstance();
        width = (int) (editor.getWidth() - x - Editor.getInstance().getRightBorder());
        height = -editor.getHeight() + editor.getDownBorder();
        selector = new SwitchMenu(Editor.DBORDER_SIZE, -Editor.DBORDER_SIZE, width - Editor.DBORDER_SIZE, height + Editor.DBORDER_SIZE, SwitchMenu.Type.DOUBLE);
        for (int yd = 0; yd < 5; yd++) {
            for (int xd = 0; xd < 5; xd++) {
                selector.addButton("testLevel.lll", null);
            }
        }
        addElement(selector);
    }
}
