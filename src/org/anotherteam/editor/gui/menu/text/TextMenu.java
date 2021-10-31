package org.anotherteam.editor.gui.menu.text;

import lombok.val;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class TextMenu extends GUIElement {

    public static final int DEFAULT_BUTTON_MENU_HEIGHT = 24;

    /**
     * Default offset for {@link TextMenu.Type HORIZONTAL} ButtonMenu
     */
    private static final Vector2i doubleOffset = new Vector2i(200, Label.DEFAULT_HEIGHT + 5);

    protected final List<TextButton> buttons;
    protected final Type type;
    protected final Vector2f buttonsOffset;

    protected final int buttonHeight;

    /**
     * Width of widest button
     */
    protected int widestButtonWidth;

    public TextMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        this.type = type;
        this.buttonsOffset = new Vector2f(0, 0);
        if (type == Type.HORIZONTAL)
            buttonHeight = height / 2 - Label.DEFAULT_HEIGHT / 2;
        else
            buttonHeight = Label.DEFAULT_HEIGHT;
        setColor(DEFAULT_COLOR);
        buttons = new ArrayList<>();

        widestButtonWidth = 0;
    }

    public void setButtonsOffset(float x, float y) {
        buttonsOffset.set(x, y);
    }

    public void addButton(String text) {
        val button = new TextButton(text, 0, 0, this);
        addButton(button);
    }

    public void addButton(String text, Runnable runnable) {
        val button = new TextButton(text, 0, 0, this);
        button.setOnClick(runnable);
        addButton(button);
    }

    public void addButton(@NotNull TextButton button) {

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
                button.setPos(buttonsOffset.x + Label.DEFAULT_TEXT_OFFSET * index + offsetX, buttonHeight);
            }
            case VERTICAL -> {
                if (widestButtonWidth < button.getWidth()) widestButtonWidth = button.getWidth();

                for (val btn : buttons) {
                    btn.setWidth(widestButtonWidth);
                }

                int offsetY = buttonHeight; // TODO this work's correct only to inverted Vertical button menu
                for (val btn : buttons)
                    offsetY += btn.getHeight();
                button.setPos(0, height - (buttonsOffset.y + buttonHeight * index + offsetY));
            }
            case DOUBLE -> {
                int maxX = width / doubleOffset.x;
                int maxY = height / doubleOffset.y;
                if (maxY < 0) maxY *= -1;
                int y = index % maxX;
                int x = index / maxX;
                if (x >= maxX) throw new LifeException("Need create pages mechanic");
                if (y >= maxY) throw new LifeException("Error with y calculating");
                button.setPos(x * doubleOffset.x, height - doubleOffset.y * y - Label.DEFAULT_HEIGHT);
            }
        }
        buttons.add(button);
    }

    @NotNull
    public Button getButton(int index) {
        return buttons.get(index);
    }

    public int getWidestButtonWidth() {
        return widestButtonWidth;
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
