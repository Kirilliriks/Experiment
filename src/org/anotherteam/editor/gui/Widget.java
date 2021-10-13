package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Widget extends GUIElement {

    private final List<GUIElement> childElements;

    private Color color;

    public Widget(String titleString, @NotNull Vector2f pos, int width, int height) {
        super(pos, width, height);
        this.pos = pos;
        this.width = width;
        this.height = height;
        val title = new Label(titleString, pos, new Vector2f(0, 0), width, height);
        childElements = new ArrayList<>();
        childElements.add(title);
        color = Color.WHITE;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        for (val element : childElements) {
            element.render(editorBatch);
        }
    }
}
