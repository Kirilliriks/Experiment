package org.anotherteam.oldeditor.object;

import org.anotherteam.oldeditor.EditorMenu;
import org.anotherteam.oldeditor.gui.menu.text.SwitchMenu;
import org.anotherteam.oldeditor.object.componentedit.ComponentEditor;
import org.anotherteam.oldeditor.object.newobject.PrefabObjectMenu;
import org.anotherteam.oldeditor.object.objectedit.GameObjectEditor;

public final class GameObjectMenu extends SwitchMenu {

    private final PrefabObjectMenu prefabObjectMenu;
    private final GameObjectEditor gameObjectEditor;
    private final ComponentEditor componentEditor;

    public GameObjectMenu(float x, float y, EditorMenu ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);

        addButton("Prefabs");
        addButton("GameObject edit");
        addButton("Component edit");
        var button = getButton(0);
        prefabObjectMenu = new PrefabObjectMenu(getWidestButtonWidth(), 0, this);
        prefabObjectMenu.setVisible(false);
        button.setOnClick((left)-> prefabObjectMenu.setVisible(true));
        button.setAfterClick((left)-> prefabObjectMenu.setVisible(false));

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

    public void chooseComponentEditor() {
        setClicked(2);
    }

    public GameObjectEditor getGameObjectEditor() {
        return gameObjectEditor;
    }

    public ComponentEditor getComponentEditor() {
        return componentEditor;
    }
}
