package org.anotherteam.editor.gui.menu.text;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public TextButton addTextButton(String text, Runnable onClick) {
        val button = new TextButton(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
        return button;
    }

    @Override
    @NotNull
    public SwitchButton addButton(String text) {
        val button = new SwitchButton(text, 0, 0, this);
        addButton(button);
        return button;
    }

    @Override
    @NotNull
    public SwitchButton addButton(String text, Runnable onClick) {
        val button = new SwitchButton(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
        return button;
    }

    @NotNull
    public SwitchButton addButton(String text, Runnable onClick, Runnable afterClick) {
        val button = new SwitchButton(text, 0, 0, this);
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

    public void setClicked(SwitchButton switchButton) {
        if (switchButton == lastClicked) return;
        if (lastClicked != null) lastClicked.setClicked(false);
        switchButton.setClicked(true);
        lastClicked = switchButton;
    }
}
