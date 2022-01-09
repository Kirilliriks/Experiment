package org.anotherteam.editor.level.room;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.room.Room;
import org.jetbrains.annotations.NotNull;

public class RoomEditor extends GUIElement {

    private final SwitchMenu selector;
    private final RoomInspector roomInspector;

    private final SwitchMenu downButtons;
    private final TextButton createEmptyButton;
    private final TextButton saveRoomsButton;
    private final TextButton deleteRoomButton;

    private Room room;

    public RoomEditor(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        selector = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE,
                (int)(width * 0.65f), height - Editor.DEFAULT_BORDER_SIZE * 3,
                SwitchMenu.Type.DOUBLE, this);
        selector.setInvertedY(true);

        roomInspector = new RoomInspector(selector.getWidth() + Editor.DEFAULT_BORDER_SIZE * 2, -Editor.DEFAULT_BORDER_SIZE,
                width - selector.getWidth() - Editor.DEFAULT_BORDER_SIZE * 3, selector.getHeight(), this);
        roomInspector.setInvertedY(true);

        downButtons = new SwitchMenu(Editor.DEFAULT_BORDER_SIZE, Editor.DEFAULT_BORDER_SIZE, 0, 0, SwitchMenu.Type.HORIZONTAL, this);

        createEmptyButton = new TextButton("Create empty room", 0, 0, downButtons);
        createEmptyButton.setOnClick(this::addEmptyRoom);
        downButtons.addButton(createEmptyButton);

        saveRoomsButton = new TextButton("Save rooms", 40, 0, downButtons);
        saveRoomsButton.setOnClick(() -> Editor.levelEditor.saveLevel());
        downButtons.addButton(saveRoomsButton);

        deleteRoomButton = new TextButton("Delete room", 40, 0, downButtons);
        deleteRoomButton.setOnClick(this::removeRoom);
        downButtons.addButton(deleteRoomButton);
    }

    public void resetRoom() {
        final Room resetRoom = Editor.levelEditor.getLevel().getRoom(room.getName());
        if (resetRoom == null)
            throw new RuntimeException("Reset room " + room.getName() + " is null");

        setRoom(resetRoom);
    }

    public void setRoom(@NotNull Room room) {
        this.room = room;

        Editor.levelEditor.getLevel().setCurrentRoom(room.getName());
        roomInspector.setRoom(room);

        Editor.gameObjectEditor.onRoomChange();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible)
            updateButtons();
    }

    public void updateButtons() {
        selector.clearChild();
        val currentLevel = Editor.levelEditor.getLevel();
        setRoom(currentLevel.getCurrentRoom());
        val rooms  = currentLevel.getRooms();

        selector.clearChild();
        for (val room : rooms) {
            val btn = selector.addButton(room.getName(),
                    ()-> {
                        if (this.room != null) {
                            roomInspector.acceptChanges();
                        }
                        setRoom(room);
                    });

            if (room.getName().equals(this.room.getName())) {
                selector.setHighlighted(btn);
            }
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }

    public void update() {
        updateButtons();
    }

    @NotNull
    public Room getRoom() {
        return room;
    }

    public void addEmptyRoom() {
        Game.levelManager.getCurrentLevel().addRoom(Room.createEmpty());
        update();
    }

    public void removeRoom() {
        Game.levelManager.getCurrentLevel().removeRoom(room);
        update();
    }

    public void renameRoom(String newName) {
        for (val btn : selector.getButtons()) {
            if (!btn.getLabelText().equals(room.getName())) continue;
            btn.setLabelText(newName);
        }

        room.setName(newName);
    }
}
