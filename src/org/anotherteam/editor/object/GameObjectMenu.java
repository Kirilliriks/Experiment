package org.anotherteam.editor.object;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.newobject.AddObjectMenu;

public final class GameObjectMenu extends SwitchMenu {

    private final AddObjectMenu addObjectMenu;

    public GameObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        addButton("Add GameObject");
        addButton("GameObject edit");
        addButton("Components edit");
        val button = (SwitchButton) getButton(0);
        this.addObjectMenu = new AddObjectMenu(getWidestButtonWidth(), 0, this);
        addObjectMenu.setVisible(false);
        button.setOnClick(()-> addObjectMenu.setVisible(true));
        button.setAfterClick(()-> addObjectMenu.setVisible(false));
        inverted = true;
    }
}
