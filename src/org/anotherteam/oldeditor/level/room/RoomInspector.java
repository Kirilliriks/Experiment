package org.anotherteam.oldeditor.level.room;

import org.anotherteam.oldeditor.Editor;
import org.anotherteam.oldeditor.gui.GUIElement;
import org.anotherteam.oldeditor.gui.Widget;
import org.anotherteam.oldeditor.gui.text.input.InputPart;
import org.anotherteam.level.room.Room;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class RoomInspector extends Widget {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    private Room inspectedRoom;

    private final InputPart nameInputLabel;

    public RoomInspector(float x, float y, int width, int height, GUIElement ownerElement) {
        super("Room inspector", x, y, width, height, ownerElement);
        flipTitle();
        color.set(DEFAULT_COLOR);
        nameInputLabel = new InputPart("Name: ", Editor.DEFAULT_BORDER_SIZE, -Editor.DEFAULT_BORDER_SIZE, this);
        nameInputLabel.setInvertedY(true);
        nameInputLabel.setAfterUnFocus(()-> RoomEditor.editor().renameRoom(nameInputLabel.getValue()));
    }

    public void acceptChanges() {
        RoomEditor.editor().renameRoom(nameInputLabel.getValue());
    }

    public void setRoom(@NotNull Room room) {
        inspectedRoom = room;
        nameInputLabel.setValue(room.getName());
    }
}