package org.anotherteam.editor.gui.menu;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SwitchMenu extends GUIElement {

    protected final List<SwitchButton> switchButtons;
    protected final Type type;

    protected SwitchButton lastClicked;

    public SwitchMenu(float x, float y, int width, int height, Type type) {
        super(x, y, width, height);
        this.type = type;
        color = new Color(150, 150, 150, 255);
        switchButtons = new ArrayList<>();
    }

    public void addButton(String text, Runnable runnable) {
        addButton(text, runnable, null);
    }

    public void addButton(String text, Runnable runnable, Runnable afterClick) {
        val button = new SwitchButton(text, 0, 0);
        button.setRunnable(runnable);
        button.setAfterClick(afterClick);
        addButton(button);
    }

    public void addButton(@NotNull SwitchButton button) {
        val index = switchButtons.size();
        int offsetX = 0;
        int offsetY = 0;
        switch (type) {
            case HORIZONTAL -> {
                if (index > 0) {
                    for (val element : switchButtons)
                        offsetX += element.getWidth();
                }
                button.setPos(5 + 5 * index + offsetX, 2);
            }
            case VERTICAL -> {
                if (index > 0) {
                    for (val element : switchButtons)
                        offsetY += element.getHeight();
                }
                button.setPos(5, 5 - 5 * index - offsetY);
            }
            case DOUBLE -> {

            }
        }
        switchButtons.add(button);
        button.setOwner(this);
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        super.update(dt);
        for (val element : switchButtons) {
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
        for (val element : switchButtons) {
            element.render(editorBatch);
        }

    }

    public enum Type {
        HORIZONTAL,
        VERTICAL,
        DOUBLE
    }
}
