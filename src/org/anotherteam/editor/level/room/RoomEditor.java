package org.anotherteam.editor.level.room;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.level.editor.LevelEditor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.room.Room;
import org.jetbrains.annotations.NotNull;

public class RoomEditor extends GUIElement {

    private static RoomEditor roomEditor;

    private final SwitchMenu selector;
    private final RoomInspector roomInspector;

    private final SwitchMenu downButtons;
    private final TextButton createEmptyButton;
    private final TextButton saveRoomsButton;
    private final TextButton deleteRoomButton;

    private static Room editedRoom;

    public RoomEditor(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        roomEditor = this;

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
        createEmptyButton.setOnClick(RoomEditor::addEmptyRoom);
        downButtons.addButton(createEmptyButton);

        saveRoomsButton = new TextButton("Save rooms", 40, 0, downButtons);
        saveRoomsButton.setOnClick(LevelEditor::saveEditableLevel);
        downButtons.addButton(saveRoomsButton);

        deleteRoomButton = new TextButton("Delete room", 40, 0, downButtons);
        deleteRoomButton.setOnClick(RoomEditor::removeRoom);
        downButtons.addButton(deleteRoomButton);
    }

    public void setRoom(@NotNull Room room) {
        LevelEditor.getEditedLevel().setCurrentRoom(room.getName());
        editedRoom = room;
        roomInspector.setRoom(room);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible)
            updateButtons();
    }

    public void updateButtons() {
        selector.clearChild();
        val currentLevel = LevelEditor.getEditedLevel();
        setRoom(currentLevel.getCurrentRoom());
        val rooms  = currentLevel.getRooms();

        selector.clearChild();
        for (val room : rooms) {
            val btn = selector.addButton(room.getName(),
                    ()-> {
                        if (editedRoom != null) {
                            roomInspector.acceptChanges();
                        }
                        setRoom(room);
                    });

            if (room.getName().equals(editedRoom.getName())) {
                selector.setHighlighted(btn);
            }
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
    }

    public static void update() {
        roomEditor.updateButtons();
    }

    @NotNull
    public static Room getEditedRoom() {
        return editedRoom;
    }

    public static void addEmptyRoom() {
        Game.levelManager.getCurrentLevel().addRoom(Room.createEmpty());
        update();
    }

    public static void removeRoom() {
        Game.levelManager.getCurrentLevel().removeRoom(editedRoom);
        update();
    }

    public static void renameRoom(String newName) {
        for (val btn : roomEditor.selector.getButtons()) {
            if (!btn.getLabelText().equals(editedRoom.getName())) continue;
            btn.setLabelText(newName);
        }

        editedRoom.setName(newName);
    }
}
