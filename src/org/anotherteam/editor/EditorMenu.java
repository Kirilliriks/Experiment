package org.anotherteam.editor;

import org.anotherteam.editor.level.LevelMenu;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.object.GameObjectMenu;

public final class EditorMenu extends SwitchMenu {

    private final LevelMenu levelMenu;
    private final GameObjectMenu gameObjectMenu;

    public EditorMenu(float x, float y, int width, int height) {
        super(x, y, width, height, Type.HORIZONTAL);
        this.levelMenu = new LevelMenu(5, -height);
        levelMenu.setVisible(false);
        addElement(levelMenu);

        this.gameObjectMenu = new GameObjectMenu(5, -height);
        gameObjectMenu.setVisible(false);
        addElement(gameObjectMenu);

        addButton("Level editor", ()-> levelMenu.setVisible(true), ()-> levelMenu.setVisible(false));
        addButton("GameObject editor", ()-> gameObjectMenu.setVisible(true), ()-> gameObjectMenu.setVisible(false));
        addButton("Open log", () -> Editor.getLog().setVisible(true));
        addButton("Close log", () -> Editor.getLog().setVisible(false));
    }
}
