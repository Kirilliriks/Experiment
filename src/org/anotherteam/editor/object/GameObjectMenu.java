package org.anotherteam.editor.object;

import org.anotherteam.editor.EditorMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.componentedit.ComponentEditor;
import org.anotherteam.editor.object.newobject.AddObjectMenu;
import org.anotherteam.editor.object.objectedit.GameObjectEditor;

public final class GameObjectMenu extends SwitchMenu {

    private final AddObjectMenu addObjectMenu;
    private final GameObjectEditor gameObjectEditor;
    private final ComponentEditor componentEditor;

    public GameObjectMenu(float x, float y, EditorMenu ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);

        addButton("Add GameObject");
        addButton("GameObject edit");
        addButton("Component edit");
        var button = getButton(0);
        addObjectMenu = new AddObjectMenu(getWidestButtonWidth(), 0, this);
        addObjectMenu.setVisible(false);
        button.setOnClick((left)-> addObjectMenu.setVisible(true));
        button.setAfterClick((left)-> addObjectMenu.setVisible(false));

        button = getButton(1);
        gameObjectEditor = new GameObjectEditor(getWidestButtonWidth(), 0, this);
        gameObjectEditor.setVisible(false);
        button.setOnClick((left)-> gameObjectEditor.setVisible(true));
        button.setAfterClick((left)-> gameObjectEditor.setVisible(false));

        button = getButton(2);
        componentEditor = new ComponentEditor(getWidestButtonWidth(), 0, this);
        componentEditor.setVisible(false);
        button.setOnClick((left)-> componentEditor.setVisible(true));
        button.setAfterClick((left)-> componentEditor.setVisible(false));

        inverted = true;

        gameObjectEditor.init();
    }

    public GameObjectEditor getGameObjectEditor() {
        return gameObjectEditor;
    }

    public ComponentEditor getComponentEditor() {
        return componentEditor;
    }
}
