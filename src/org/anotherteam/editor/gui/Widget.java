package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.util.Color;;

public class Widget extends GUIElement {

    public Widget(String titleString, float x, float y, int width, int height) {
        super(x, y, width, height);
        val title = new Label(titleString, 0, height - 0.1f);
        title.setColor(new Color(100, 100, 100, 255));
        addElement(title);
        color = Color.GRAY;
    }
}
