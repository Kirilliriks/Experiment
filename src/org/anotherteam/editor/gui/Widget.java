package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Widget extends GUIElement {

    private final List<GUIElement> childElements;

    public Widget(String titleString, @NotNull Vector2f pos, int width, int height) {
        super(pos, width, height);
        this.pos = pos;
        this.width = width;
        this.height = height;
        val title = new Label(titleString, new Vector2f(0, height), 10);
        childElements = new ArrayList<>();
        addElement(title);
        color = Color.GRAY;
    }

    public void addElement(@NotNull GUIElement element) {
        childElements.add(element);
        element.setOwner(this);
    }

    @Override
    public void update(float dt) {
        for (val element : childElements) {
            element.update(dt);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        for (val element : childElements) {
            element.render(editorBatch);
        }
    }
}
