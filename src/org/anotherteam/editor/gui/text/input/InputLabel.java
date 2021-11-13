package org.anotherteam.editor.gui.text.input;

import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.util.Color;

public final class InputLabel extends Label {

    public static final Color DEFAULT_COLOR = new Color(128, 128, 200);

    private final InputPart inputPart;

    public InputLabel(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        setColor(DEFAULT_COLOR);
        inputPart = new InputPart("INPUT HERE", width, 0, this);
    }

    public void setInputText(String text) {
        inputPart.setLabelText(text);
    }
}
