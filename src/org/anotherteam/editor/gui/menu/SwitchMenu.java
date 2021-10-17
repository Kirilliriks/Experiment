package org.anotherteam.editor.gui.menu;

import lombok.val;


public class SwitchMenu extends ButtonMenu {

    protected SwitchButton lastClicked;

    public SwitchMenu(float x, float y, int width, int height, Type type) {
        super(x, y, width, height, type);
    }

    @Override
    public void addButton(String text, Runnable runnable) {
        val button = new SwitchButton(text, 0, 0);
        button.setRunnable(runnable);
        addButton(button);
    }

    public void addButton(String text, Runnable runnable, Runnable afterClick) {
        val button = new SwitchButton(text, 0, 0);
        button.setRunnable(runnable);
        button.setAfterClick(afterClick);
        addButton(button);
    }


    public void setClicked(SwitchButton switchButton) {
        if (switchButton == lastClicked) return;
        if (lastClicked != null) lastClicked.setClicked(false);
        lastClicked = switchButton;
    }
}
