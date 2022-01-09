package org.anotherteam.editor.gui;

import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.util.Color;

public class Widget extends GUIElement {

    private final Label title;

    public Widget(String titleString, float x, float y, int width, int height, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        title = new Label(titleString, 0, height - 0.1f, this);
        title.setColor(80, 80, 80, 255);
        setColor(Color.gray());
    }

    /**
     * Flip title Label position in X axis
     */
    public void flipTitle() {
        if (title.pos.x > 0) {
            title.pos.x = 0;
        } else {
            title.pos.x = width - title.getWidth();
        }
    }
}
