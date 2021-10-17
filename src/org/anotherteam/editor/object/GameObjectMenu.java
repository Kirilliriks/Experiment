package org.anotherteam.editor.object;

import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.object.GameObject;

public final class GameObjectMenu extends SwitchMenu {

    private GameObject selectedObject;

    public GameObjectMenu(float x, float y) {
        super(x, y, 0, 0, Type.VERTICAL);
        addButton("GameObject edit", null);
        addButton("Components edit", null);

        selectedObject = null;
    }
}
