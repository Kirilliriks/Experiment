package org.anotherteam.editor.gui.menu;

import lombok.val;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ButtonMenu extends GUIElement {

    private static final Vector2i buttonSize = new Vector2i(200, Label.HEIGHT + 5);

    protected final List<Button> buttons;
    protected final Type type;

    public ButtonMenu(float x, float y, int width, int height, Type type) {
        super(x, y, width, height);
        this.type = type;
        color = new Color(150, 150, 150, 255);
        buttons = new ArrayList<>();
    }

    public void addButton(String text, Runnable runnable) {
        val button = new Button(text, 0, 0);
        button.setRunnable(runnable);
        addButton(button);
    }

    public void addButton(@NotNull Button button) {
        int index = buttons.size();
        if (contains(button.getText())) {
            for (val btn : buttons) {
                if (!button.getText().equals(btn.getText())) continue;
                index = buttons.indexOf(btn);
                break;
            }
        }

        int offsetX = 0;
        int offsetY = 0;
        switch (type) {
            case HORIZONTAL -> {
                if (index > 0) {
                    for (val element : buttons)
                        offsetX += element.getWidth();
                }
                button.setPos(5 + 5 * index + offsetX, 2); // TODO y = 0, don't increase x by 5, use special widget to offset SwitchMenu's buttons, like MenuBar
            }
            case VERTICAL -> {
                if (index > 0) {
                    for (val element : buttons)
                        offsetY += element.getHeight();
                }
                button.setPos(0, -5 * index - offsetY);
            }
            case DOUBLE -> {
                int maxX = width / buttonSize.x;
                int maxY = height / buttonSize.y;
                if (maxY < 0) maxY *= -1;
                int y  = index % maxX;
                int x = (index - y) / maxX;
                if (x >= maxX) throw new LifeException("Need create pages mechanic");
                if (y >= maxY) throw new LifeException("Error with y calculating");
                button.setPos(x * buttonSize.x, -buttonSize.y * y - Label.HEIGHT);
            }
        }
        buttons.add(button);
        button.setOwner(this);
    }

    public boolean contains(String text) {
        for (val button : buttons) {
            if (button.getText().equals(text)) return true;
        }
        return false;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        for (val element : buttons) {
            if (!element.isVisible()) continue;
            element.update(dt);
            element.updateElements(dt);
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        for (val element : buttons) {
            element.render(editorBatch);
        }
    }

    public enum Type {
        HORIZONTAL,
        VERTICAL,
        DOUBLE
    }
}
