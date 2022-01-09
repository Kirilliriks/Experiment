package org.anotherteam.editor.level.room;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Widget;
import org.anotherteam.editor.gui.text.input.InputLabel;
import org.anotherteam.level.room.Room;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class RoomInspector extends Widget {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    private Room inspectedRoom;

    private final InputLabel nameInputLabel;

    public RoomInspector(float x, float y, int width, int height, GUIElement ownerElement) {
        super("Room inspector", x, y, width, height, ownerElement);
        flipTitle();
        color.set(DEFAULT_COLOR);
        nameInputLabel = new InputLabel("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(()-> RoomEditor.renameRoom(nameInputLabel.getInputText()));
    }

    public void acceptChanges() {
        RoomEditor.renameRoom(nameInputLabel.getInputText());
    }

    public void setRoom(@NotNull Room room) {
        inspectedRoom = room;
        nameInputLabel.setInputText(room.getName());
    }
}