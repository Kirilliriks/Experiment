package org.anotherteam.editor.level.selector;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.room.Room;
import org.jetbrains.annotations.NotNull;

public class RoomSelector extends GUIElement {

    private final SwitchMenu selector;

    private final SwitchMenu downButtons;
    private final TextButton createEmptyButton;
    private final TextButton saveLevelButton;

    public RoomSelector(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        selector = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                width - Editor.DEFAULT_BORDER_SIZE * 2, height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        selector.setYInverted(true);

        downButtons = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);

        createEmptyButton = new TextButton("Create empty room", 0, 0, downButtons);
        createEmptyButton.setOnClick(()-> {
            Game.levelManager.getCurrentLevel().addRoom(Room.createEmpty());
            fillButtons();
        });
        downButtons.addButton(createEmptyButton);

        saveLevelButton = new TextButton("Save rooms", 40, 0, downButtons);
        saveLevelButton.setOnClick(()-> Game.levelManager.saveLevel());
        downButtons.addButton(saveLevelButton);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible)
            fillButtons();
    }

    public void fillButtons() {
        selector.clearChild();
        val currentLevel = Game.levelManager.getCurrentLevel();
        val rooms  = currentLevel.getRooms();

        for (val room : rooms) {
            val btn = selector.addButton(room.getName(),
                    ()-> currentLevel.setCurrentRoom(room));

            if (Game.levelManager.getCurrentRoom().getName().equals(room.getName()))
                selector.setClicked(btn);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }
}
