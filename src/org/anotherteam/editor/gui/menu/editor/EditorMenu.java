package org.anotherteam.editor.gui.menu.editor;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.menu.SwitchMenu;

public final class EditorMenu extends SwitchMenu {

    private final LevelMenu levelMenu;

    public EditorMenu(float x, float y, int width, int height) {
        super(x, y, width, height, Type.HORIZONTAL);
        this.levelMenu = new LevelMenu(5, -height);
        levelMenu.setVisible(false);
        addElement(levelMenu);

        addButton("Level editor", ()-> levelMenu.setVisible(true), ()-> levelMenu.setVisible(false));
        addButton("Open log", () -> Editor.getLog().setVisible(true));
        addButton("Close log", () -> Editor.getLog().setVisible(false));
    }
}
