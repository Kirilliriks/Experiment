package org.anotherteam.editor.gui.menu;

import lombok.val;
import org.anotherteam.editor.gui.GUIElement;

public class SwitchMenu extends ButtonMenu {

    protected SwitchButton lastClicked;

    public SwitchMenu(float x, float y, int width, Type type, GUIElement ownerElement) {
        super(x, y, width, DEFAULT_BUTTON_MENU_HEIGHT, type, ownerElement);
    }

    public SwitchMenu(float x, float y, int width, int height, Type type, GUIElement ownerElement) {
        super(x, y, width, height, type, ownerElement);
    }

    @Override
    public void addButton(String text) {
        val button = new SwitchButton(text, 0, 0, this);
        addButton(button);
    }

    @Override
    public void addButton(String text, Runnable onClick) {
        val button = new SwitchButton(text, 0, 0, this);
        button.setOnClick(onClick);
        addButton(button);
    }

    public void addButton(String text, Runnable onClick, Runnable afterClick) {
        val button = new SwitchButton(text, 0, 0, this);
        button.setOnClick(onClick);
        button.setAfterClick(afterClick);
        addButton(button);
    }

    public void setClicked(SwitchButton switchButton) {
        if (switchButton == lastClicked) return;
        if (lastClicked != null) lastClicked.setClicked(false);
        lastClicked = switchButton;
    }
}
