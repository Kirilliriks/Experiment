package org.anotherteam.editor.object;

import org.anotherteam.editor.EditorMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.newobject.AddObjectMenu;
import org.anotherteam.editor.object.objectedit.GameObjectEditor;

public final class GameObjectMenu extends SwitchMenu {

    private static GameObjectMenu gameObjectMenu;

    private final AddObjectMenu addObjectMenu;
    private final GameObjectEditor gameObjectEditor;

    public GameObjectMenu(float x, float y, EditorMenu ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        gameObjectMenu = this;

        addButton("Add GameObject");
        addButton("GameObject edit");
        addButton("Components edit");
        var button = getButton(0);
        addObjectMenu = new AddObjectMenu(getWidestButtonWidth(), 0, this);
        addObjectMenu.setVisible(false);
        button.setOnClick(()-> addObjectMenu.setVisible(true));
        button.setAfterClick(()-> addObjectMenu.setVisible(false));

        button = getButton(1);
        gameObjectEditor = new GameObjectEditor(getWidestButtonWidth(), 0, this);
        gameObjectEditor.setVisible(false);
        button.setOnClick(()-> gameObjectEditor.setVisible(true));
        button.setAfterClick(()-> gameObjectEditor.setVisible(false));

        inverted = true;
    }

    public GameObjectEditor getGameObjectEditor() {
        return gameObjectEditor;
    }
}
