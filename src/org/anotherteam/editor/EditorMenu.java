package org.anotherteam.editor;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.editor.level.LevelMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.object.GameObjectMenu;
import org.jetbrains.annotations.NotNull;

public final class EditorMenu extends SwitchMenu {

    private final LevelMenu levelMenu;
    private final GameObjectMenu gameObjectMenu;

    public EditorMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, DEFAULT_BUTTON_MENU_HEIGHT, Type.HORIZONTAL, ownerElement);
        levelMenu = new LevelMenu(Label.DEFAULT_TEXT_OFFSET, -height - height * 0.4f, this);
        levelMenu.setClicked(levelMenu.getButton(0));
        levelMenu.setVisible(false);

        gameObjectMenu = new GameObjectMenu(Label.DEFAULT_TEXT_OFFSET, -height - height * 0.4f, this);
        gameObjectMenu.setClicked(gameObjectMenu.getButton(0));
        gameObjectMenu.setVisible(false);

        addButton("Level editor", (info)-> levelMenu.setVisible(true), (info)-> levelMenu.setVisible(false));
        addButton("GameObject editor", (info)-> gameObjectMenu.setVisible(true), (info)-> gameObjectMenu.setVisible(false));
        addTextButton("Open log", (info) -> Editor.log().setVisible(true));
        addTextButton("Close log", (info) -> Editor.log().setVisible(false));
        setClicked(getButton(0));
    }

    @NotNull
    public LevelMenu getLevelMenu() {
        return levelMenu;
    }

    @NotNull
    public GameObjectMenu getGameObjectMenu() {
        return gameObjectMenu;
    }
}
