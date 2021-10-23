package org.anotherteam.editor.object;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.SwitchButton;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.object.newobject.NewObjectMenu;

public final class GameObjectMenu extends SwitchMenu {


    private final NewObjectMenu newObjectMenu;

    public GameObjectMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        var button = new SwitchButton("New GameObject", 0, 0, this);
        this.newObjectMenu = new NewObjectMenu(0, 0, button);
        newObjectMenu.setVisible(false);
        button.setOnClick(()-> newObjectMenu.setVisible(true));
        button.setAfterClick(()-> newObjectMenu.setVisible(false));
        addButton(button);
        addButton("GameObject edit", null);
        addButton("Components edit", null);
    }
}
