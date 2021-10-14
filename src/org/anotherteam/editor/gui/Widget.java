package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Widget extends GUIElement {

    private final List<GUIElement> childElements;

    public Widget(String titleString, float x, float y, int width, int height) {
        super(x, y, width, height);
        val title = new Label(titleString, 0, height - 0.1f, 10);
        title.setColor(new Color(100, 100, 100, 255));
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
        if (!visible) return;

        for (val element : childElements) {
            if (!element.visible) continue;
            element.update(dt);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        for (val element : childElements) {
            element.render(editorBatch);
        }
    }
}
