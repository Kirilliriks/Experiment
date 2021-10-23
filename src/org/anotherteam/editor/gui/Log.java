package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.util.Color;

import java.util.ArrayList;
import java.util.List;

public final class Log extends Widget {

    private final List<Label> logStrings;

    public Log(float x, float y, int width, int height, GUIElement ownerElement) {
        super("Log", x, y, width, height, ownerElement);
        logStrings = new ArrayList<>();
        color = new Color(120, 120, 120, 255);
        val stringsCount = height / 12;
        for (int i = 0; i < stringsCount; i++) {
            val label = new Label("", 2, 2 + i * 12, this);
            label.setColor(color);
            logStrings.add(label);
        }
    }

    public void addMessage(String text) {
        for (int i = logStrings.size() - 1; i > 0; i--)
            logStrings.get(i).setText(logStrings.get(i - 1).text);
        logStrings.get(0).setText(text);
    }
}
