package org.anotherteam.editor.level;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.SwitchButton;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.level.selector.LevelSelector;

public final class LevelMenu extends SwitchMenu {

    private final LevelSelector levelSelector;

    public LevelMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        addButton("Select level");
        addButton("Select room");
        addButton("Tile viewer");
        var button = (SwitchButton) getButton(0);
        levelSelector = new LevelSelector(getWidestButtonWidth(), 0, this);
        levelSelector.setVisible(false);
        button.setOnClick(()-> levelSelector.setVisible(true));
        button.setAfterClick(()-> levelSelector.setVisible(false));
        inverted = true;
    }
}
