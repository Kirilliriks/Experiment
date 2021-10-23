package org.anotherteam.editor.gui.menu;

import lombok.val;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ButtonMenu extends GUIElement {

    public static final int DEFAULT_GUI_HEIGHT = 24;

    private static final Vector2i defaultMaxButtonSize = new Vector2i(200, Label.DEFAULT_HEIGHT + 5);

    protected final List<Button> buttons;
    protected final Type type;

    protected final int buttonHeight;

    /**
     * Width of widest button
     */
    protected int widestButtonWidth;

    public ButtonMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        this.type = type;
        if (type == Type.HORIZONTAL)
            buttonHeight = height / 2 - Label.DEFAULT_HEIGHT / 2;
        else
            buttonHeight = Label.DEFAULT_HEIGHT;
        color = DEFAULT_COLOR;
        buttons = new ArrayList<>();

        widestButtonWidth = 0;
    }

    @Override
    public void setInverted(boolean inverted) {
        super.setInverted(inverted);
        for (val btn : buttons)
            btn.setInverted(inverted);
    }

    public void addButton(String text, Runnable runnable) {
        val button = new Button(text, 0, 0, this);
        button.setOnClick(runnable);
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

        switch (type) {
            case HORIZONTAL -> {
                int offsetX = 0;
                for (val btn : buttons)
                    offsetX += btn.getWidth();
                button.setPos(5 + 5 * index + offsetX, buttonHeight); // TODO y = 0, don't increase x by 5, use special widget to offset SwitchMenu's buttons, like MenuBar
            }
            case VERTICAL -> {
                if (widestButtonWidth < button.getWidth())
                    widestButtonWidth = button.getWidth();
                for (val btn : buttons) {
                    btn.setWidth(widestButtonWidth);
                    for (val child : btn.getChildElements())
                        child.setPosX(btn.getWidth());
                }

                int offsetY = 0;
                for (val btn : buttons)
                    offsetY += btn.getHeight();
                button.setPos(0, -buttonHeight * index - offsetY);
            }
            case DOUBLE -> {
                int maxX = width / defaultMaxButtonSize.x;
                int maxY = height / defaultMaxButtonSize.y;
                if (maxY < 0) maxY *= -1;
                int y = maxY - index % maxX - 1;
                int x = index / maxX;
                if (x >= maxX) throw new LifeException("Need create pages mechanic");
                if (y >= maxY) throw new LifeException("Error with y calculating");
                button.setPos(x * defaultMaxButtonSize.x, Label.DEFAULT_HEIGHT + defaultMaxButtonSize.y * y);
            }
        }
        buttons.add(button);
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
