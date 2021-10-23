package org.anotherteam.editor.object.newobject;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.ButtonMenu;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.util.Color;

public final class NewObjectMenu extends GUIElement {

    private final SwitchMenu typeMenu;

    public NewObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        typeMenu = new SwitchMenu(0, height - SwitchMenu.DEFAULT_GUI_HEIGHT, width, ButtonMenu.Type.HORIZONTAL, this);
        typeMenu.setColor(new Color(100, 100, 100, 255));
    }
}
