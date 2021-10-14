package org.anotherteam.editor.gui.barmenu;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SwitchMenu extends GUIElement {

    private final List<SwitchButton> elements;
    private SwitchButton lastClicked;

    public SwitchMenu(float x, float y, int width, int height) {
        super(x, y, width, height);
        color = new Color(150, 150, 150, 255);
        elements = new ArrayList<>();
    }

    public void addButton(String text, Runnable runnable) {
        val index = elements.size();
        int offsetX = 0;
        if (index > 0) {
            for (val element : elements)
            offsetX += element.getWidth();
        }
        val button = new SwitchButton(text, 5 + 5 * index + offsetX, 2, 12);
        button.setRunnable(runnable);
        elements.add(button);
        button.setOwner(this);
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        for (val element : elements) {
            if (!element.isVisible()) continue;
            element.update(dt);
        }
    }

    public void setClicked(SwitchButton switchButton) {
        if (switchButton == lastClicked) return;
        if (lastClicked != null) lastClicked.setClicked(false);
        lastClicked = switchButton;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        for (val element : elements) {
            element.render(editorBatch);
        }
    }
}
