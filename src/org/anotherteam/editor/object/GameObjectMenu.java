package org.anotherteam.editor.object;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.SwitchButton;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.object.newobject.NewObjectMenu;

public final class GameObjectMenu extends SwitchMenu {


    private final NewObjectMenu newObjectMenu;

    public GameObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        addButton("New GameObject");
        addButton("GameObject edit");
        addButton("Components edit");
        val button = (SwitchButton)getButton(0);
        this.newObjectMenu = new NewObjectMenu(getWidestButtonWidth(), 0, button);
        newObjectMenu.setVisible(false);
        button.setOnClick(()-> newObjectMenu.setVisible(true));
        button.setAfterClick(()-> newObjectMenu.setVisible(false));
        inverted = true;
    }
}
