package org.anotherteam.editor.gui;

import lombok.val;

import java.util.ArrayList;
import java.util.List;

public final class Log extends Widget {

    private final List<Label> logStrings;

    public Log(float x, float y, int width, int height) {
        super("Log", x, y, width, height);
        logStrings = new ArrayList<>();
        val stringsCount = height / 12;
        for (int i = 0; i < stringsCount; i++) {
            val label = new Label("TEST", 2, 2 + i * 12, 10);
            logStrings.add(label);
            addElement(label);
        }
    }

    public void addMessage(String text) {
        for (int i = logStrings.size() - 1; i > 0; i--)
            logStrings.get(i).setText(logStrings.get(i - 1).text);
        logStrings.get(0).setText(text);
    }
}
