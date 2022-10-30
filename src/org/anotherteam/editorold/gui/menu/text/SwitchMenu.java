package org.anotherteam.editorold.gui.menu.text;

import org.anotherteam.editorold.gui.Button;
import org.anotherteam.editorold.gui.GUIElement;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwitchMenu extends TextMenu {

    public static final Color DEFAULT_COLOR = new Color(120, 120, 120);

    protected SwitchButton lastClicked;

    public SwitchMenu(float x, float y, int width, Type type, GUIElement ownerElement) {
        super(x, y, width, DEFAULT_BUTTON_MENU_HEIGHT, type, ownerElement);
        lastClicked = null;
        setColor(DEFAULT_COLOR);
    }

    public SwitchMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, type, ownerElement);
        lastClicked = null;
        setColor(DEFAULT_COLOR);
    }

    @Nullable
    public SwitchButton getLastClicked() {
        return lastClicked;
    }

    @NotNull
    public TextButton addTextButton(String text, Button.Click onClick) {
        final var button = new TextButton(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
        return button;
    }

    @Override
    @NotNull
    public SwitchButton addButton(String text) {
        final var button = new SwitchButton(text, 0, 0, this);
        addButton(button);
        return button;
    }

    @Override
    @NotNull
    public SwitchButton addButton(String text, Button.Click onClick) {
        final var button = new SwitchButton(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
        return button;
    }

    @NotNull
    public SwitchButton addButton(String text, Button.Click onClick, Button.Click afterClick) {
        final var button = new SwitchButton(text, 0, 0, this);
        button.setOnClick(onClick);
        button.setAfterClick(afterClick);
        addButton(button);
        return button;
    }

    @Override
    public @NotNull SwitchButton getButton(int index) {
        return (SwitchButton) super.getButton(index);
    }

    public void setHighlighted(SwitchButton switchButton) {
        if (switchButton == lastClicked) return;
        if (lastClicked != null) lastClicked.setClicked(false);
        switchButton.setHighlighted();
        lastClicked = switchButton;
    }

    public void setClicked(int index) {
        setClicked(getButton(index), true);
    }

    public void setClicked(int index, boolean left) {
        setClicked(getButton(index), left);
    }

    public void setClicked(SwitchButton switchButton) {
        setClicked(switchButton, true);
    }

    public void setClicked(SwitchButton switchButton, boolean left) {
        if (switchButton == null) return;

        if (lastClicked != null && switchButton != lastClicked) {
            lastClicked.setClicked(false);
        }

        switchButton.setClicked(true, left);
        lastClicked = switchButton;
    }
}
