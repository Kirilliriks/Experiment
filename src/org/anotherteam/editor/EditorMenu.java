package org.anotherteam.editor;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.level.LevelMenu;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.object.GameObjectMenu;

public final class EditorMenu extends SwitchMenu {

    private final LevelMenu levelMenu;
    private final GameObjectMenu gameObjectMenu;

    public EditorMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, DEFAULT_GUI_HEIGHT, Type.HORIZONTAL, ownerElement);
        this.levelMenu = new LevelMenu(5, -height - height * 0.4f, this);
        levelMenu.setVisible(false);

        this.gameObjectMenu = new GameObjectMenu(5, -height - height * 0.4f, this);
        gameObjectMenu.setVisible(false);

        addButton("Level editor", ()-> levelMenu.setVisible(true), ()-> levelMenu.setVisible(false));
        addButton("GameObject editor", ()-> gameObjectMenu.setVisible(true), ()-> gameObjectMenu.setVisible(false));
        addButton("Open log", () -> Editor.log().setVisible(true));
        addButton("Close log", () -> Editor.log().setVisible(false));
    }
}
