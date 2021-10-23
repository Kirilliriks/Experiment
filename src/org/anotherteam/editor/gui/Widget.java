package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.util.Color;;

public class Widget extends GUIElement {

    public Widget(String titleString, float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        val title = new Label(titleString, 0, height - 0.1f, this);
        title.setColor(new Color(100, 100, 100, 255));
        color = Color.GRAY;
    }
}
