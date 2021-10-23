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
        width = (int) (editor.getWidth() - x - Editor.getInstance().getRightBorderSize());
        height = editor.getHeight() - editor.getUpBorderSize();
        inverted = true;

        typeMenu = new SwitchMenu(0, height, width, ButtonMenu.Type.HORIZONTAL, this);
        typeMenu.setColor(new Color(255, 150, 150, 255));
    }
}
