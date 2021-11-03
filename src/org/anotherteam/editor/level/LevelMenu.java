package org.anotherteam.editor.level;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.level.selector.LevelSelector;
import org.anotherteam.editor.level.selector.RoomSelector;
import org.jetbrains.annotations.NotNull;

public final class LevelMenu extends SwitchMenu {

    private final LevelSelector levelSelector;
    private final RoomSelector roomSelector;

    public LevelMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);
        addButton("Select level");
        addButton("Select room");
        addButton("Tile viewer");
        var button = getButton(0);
        levelSelector = new LevelSelector(getWidestButtonWidth(), 0, this);
        levelSelector.setVisible(false);
        button.setOnClick(()-> {
            levelSelector.fillButtons();
            levelSelector.setVisible(true);
        });
        button.setAfterClick(()-> levelSelector.setVisible(false));

        button = getButton(1);
        roomSelector = new RoomSelector(getWidestButtonWidth(), 0, this);
        roomSelector.setVisible(false);
        button.setOnClick(()-> {
            roomSelector.fillButtons();
            roomSelector.setVisible(true);
        });
        button.setAfterClick(()-> roomSelector.setVisible(false));
        inverted = true;
    }

    @NotNull
    public LevelSelector getLevelSelector() {
        return levelSelector;
    }

    @NotNull
    public RoomSelector getRoomSelector() {
        return roomSelector;
    }
}
