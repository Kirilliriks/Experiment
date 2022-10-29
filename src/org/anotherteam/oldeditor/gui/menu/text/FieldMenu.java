package org.anotherteam.oldeditor.gui.menu.text;

import org.anotherteam.oldeditor.gui.Button;
import org.anotherteam.oldeditor.gui.GUIElement;
import org.anotherteam.oldeditor.gui.text.input.InputPart;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class FieldMenu extends TextMenu {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    public FieldMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, type, ownerElement);
        setColor(DEFAULT_COLOR);
    }

    @Override
    @NotNull
    public InputPart addButton(String text) {
        final InputPart button = new InputPart(text, 0, 0, this);
        addButton(button);
        return button;
    }

    @Override
    @NotNull
    public InputPart addButton(String text, Button.Click onClick) {
        final InputPart button = new InputPart(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
        return button;
    }

    @Override
    public @NotNull InputPart getButton(int index) {
        return (InputPart) super.getButton(index);
    }
}
