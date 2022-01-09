package org.anotherteam.editor.object.objectedit;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;

public final class GameObjectEdit extends GUIElement {

    public GameObjectEdit(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);

        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;
    }
}
