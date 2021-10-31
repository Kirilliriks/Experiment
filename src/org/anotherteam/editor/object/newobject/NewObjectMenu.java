package org.anotherteam.editor.object.newobject;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;

public final class NewObjectMenu extends GUIElement {

    private final SwitchMenu typeMenu;

    public NewObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        typeMenu = new SwitchMenu(0, height - SwitchMenu.DEFAULT_BUTTON_MENU_HEIGHT, width, TextMenu.Type.HORIZONTAL, this);
        typeMenu.setColor(100, 100, 100, 255);
        typeMenu.setButtonsOffset(Label.DEFAULT_TEXT_OFFSET, 0);
        typeMenu.addButton("Entity");
        typeMenu.addButton("Item");
        typeMenu.addButton("Light");
        typeMenu.addButton("Collider");
    }
}
