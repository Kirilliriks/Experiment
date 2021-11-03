package org.anotherteam.editor.level;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.level.selector.LevelSelector;
import org.jetbrains.annotations.NotNull;

public final class LevelMenu extends SwitchMenu {

    private final LevelSelector levelSelector;

    public LevelMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        addButton("Select level");
        addButton("Select room");
        addButton("Tile viewer");
        var button = (SwitchButton) getButton(0);
        levelSelector = new LevelSelector(getWidestButtonWidth(), 0, button);
        levelSelector.setVisible(false);
        button.setOnClick(()-> levelSelector.setVisible(true));
        button.setAfterClick(()-> levelSelector.setVisible(false));
        inverted = true;
    }

    @NotNull
    public LevelSelector getLevelSelector() {
        return levelSelector;
    }
}
