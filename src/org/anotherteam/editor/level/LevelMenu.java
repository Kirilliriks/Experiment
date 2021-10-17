package org.anotherteam.editor.level;

import org.anotherteam.editor.gui.menu.SwitchButton;
import org.anotherteam.editor.gui.menu.SwitchMenu;
import org.anotherteam.editor.level.selector.LevelSelector;

public final class LevelMenu extends SwitchMenu {

    // Child of Select level button, not levelMenu
    private final LevelSelector levelSelector;

    public LevelMenu(float x, float y) {
        super(x, y, 0, 0, Type.VERTICAL);
        var button = new SwitchButton("Select level", 0, 0);
        levelSelector = new LevelSelector(button.getWidth(), button.getHeight());
        button.setRunnable(()-> levelSelector.setVisible(true));
        button.setAfterClick(()-> levelSelector.setVisible(false));
        levelSelector.setVisible(false);
        button.addElement(levelSelector);
        addButton(button);
        addButton("Select room", null);
        addButton("Tile viewer", null);
    }
}
