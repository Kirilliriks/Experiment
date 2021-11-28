package org.anotherteam.editor.gui.menu.text;

import lombok.val;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
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
    private static final Vector2i DOUBLE_BUTTON_OFFSET = new Vector2i(200, Label.DEFAULT_HEIGHT + 5);

    protected final List<TextButton> buttons;
    protected final Type type;
    protected final Vector2f startOffset;

    protected final int buttonHeight;

    /**
     * Width of widest button
     */
    protected int widestButtonWidth;

    public TextMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, ownerElement);
        this.type = type;
        this.startOffset = new Vector2f(0, 0);
        if (type == Type.HORIZONTAL)
            buttonHeight = height / 2 - Label.DEFAULT_HEIGHT / 2;
        else
            buttonHeight = Label.DEFAULT_HEIGHT;
        setColor(DEFAULT_COLOR);
        buttons = new ArrayList<>();

        widestButtonWidth = 0;
    }

    public void setStartOffset(float x, float y) {
        startOffset.set(x, y);
    }

    @NotNull
    public TextButton addButton(String text) {
        val button = new TextButton(text, 0, 0, this);
        addButton(button);
        return button;
    }

    public TextButton addButton(String text, Runnable runnable) {
        val button = new TextButton(text, 0, 0, this);
        button.setOnClick(runnable);
        addButton(button);
        return button;
    }

    @NotNull
    public TextButton addButton(@NotNull TextButton button) {

        int index = buttons.size();
        if (contains(button.getLabelText())) {
            for (val btn : buttons) {
                if (!button.getLabelText().equals(btn.getLabelText())) continue;

                index = buttons.indexOf(btn);
                button = btn;
                break;
            }
        }

        switch (type) {
            case HORIZONTAL -> {
                int offsetX = 0;
                for (val btn : buttons)
                    offsetX += btn.getWidth();
                button.setPos(startOffset.x + Label.DEFAULT_TEXT_OFFSET * index + offsetX, buttonHeight);
            }
            case VERTICAL -> {
                if (widestButtonWidth < button.getWidth()) widestButtonWidth = button.getWidth();

                for (val btn : buttons) {
                    btn.setWidth(widestButtonWidth);
                }

                int offsetY = buttonHeight; // TODO this work's correct only to inverted Vertical button menu
                for (val btn : buttons)
                    offsetY += btn.getHeight();
                button.setPos(0, height - (startOffset.y + buttonHeight * index + offsetY));
            }
            case DOUBLE -> {
                val sizeX = width / DOUBLE_BUTTON_OFFSET.x;
                val sizeY = height / DOUBLE_BUTTON_OFFSET.y;
                if (index >= sizeX * sizeY) throw new LifeException("Need create pages mechanic");

                // X and Y are specially swapped
                int y = index % sizeY;
                int x = (index - y) / sizeX;
                button.setPos(x * DOUBLE_BUTTON_OFFSET.x,
                              height - (y + 1) * DOUBLE_BUTTON_OFFSET.y);
            }
        }

        if (!buttons.contains(button))
            buttons.add(button);
        return button;
    }

    @Override
    public void clearChild() {
        super.clearChild();
        buttons.clear();
    }

    @NotNull
    public Button getButton(int index) {
        return buttons.get(index);
    }

    @NotNull
    public List<TextButton> getButtons() {
        return buttons;
    }

    public int getWidestButtonWidth() {
        return widestButtonWidth;
    }

    public boolean contains(String text) {
        for (val button : buttons) {
            if (button.getLabelText().equals(text)) return true;
        }
        return false;
    }

    public enum Type {
        HORIZONTAL,
        VERTICAL,
        DOUBLE
    }
}
