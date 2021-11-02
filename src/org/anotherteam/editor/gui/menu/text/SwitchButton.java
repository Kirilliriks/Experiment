package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.GUIElement;
import org.lwjgl.glfw.GLFW;

public class SwitchButton extends TextButton {

    private SwitchMenu switchMenu;
    private Runnable afterClick;

    public SwitchButton(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        clicked = false;
        switchMenu = (SwitchMenu) ownerElement;
    }

    public void setAfterClick(Runnable afterClick) {
        this.afterClick = afterClick;
    }

    @Override
    public void setClicked(boolean clicked) {
        if (!clicked && afterClick != null)
            afterClick.run();
        this.clicked = clicked;

        if (!clicked) {
            text.getColor().r = 0;
            return;
        }
        if (onClick != null) onClick.run();
        text.getColor().r = 255;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (clicked) return;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        switchMenu.setClicked(this);
    }
}
