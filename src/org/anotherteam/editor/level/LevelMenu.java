package org.anotherteam.editor.level;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.level.editor.LevelEditor;
import org.anotherteam.editor.level.room.RoomEditor;
import org.jetbrains.annotations.NotNull;

public final class LevelMenu extends SwitchMenu {

    private final LevelEditor levelEditor;
    private final RoomEditor roomEditor;
    private final TileViewer tileViewer;

    public LevelMenu(float x, float y, GUIElement ownerElement) {
        super(x, y, 0, 0, Type.VERTICAL, ownerElement);

        addButton("Select level");
        addButton("Select room");
        addButton("Tile viewer");

        var button = getButton(0);
        levelEditor = new LevelEditor(getWidestButtonWidth(), 0, this);
        levelEditor.setVisible(false);
        button.setOnClick((info)-> levelEditor.setVisible(true));
        button.setAfterClick((info)-> levelEditor.setVisible(false));

        button = getButton(1);
        roomEditor = new RoomEditor(getWidestButtonWidth(), 0, this);
        roomEditor.setVisible(false);
        button.setOnClick((info)-> roomEditor.setVisible(true));
        button.setAfterClick((info)-> roomEditor.setVisible(false));

        button = getButton(2);
        tileViewer = new TileViewer(getWidestButtonWidth(), 0, this);
        tileViewer.setVisible(false);
        button.setOnClick((info)-> tileViewer.setVisible(true));
        button.setAfterClick((info)-> tileViewer.setVisible(false));

        inverted = true;
    }

    @NotNull
    public LevelEditor getLevelEditor() {
        return levelEditor;
    }

    @NotNull
    public RoomEditor getRoomEditor() {
        return roomEditor;
    }
}
