package org.anotherteam.editor.gui;

import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class Widget {

    private Vector2f pos;
    private int width, height;
    private Color color;

    public Widget(@NotNull Vector2f pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        color = Color.WHITE;
    }
}
