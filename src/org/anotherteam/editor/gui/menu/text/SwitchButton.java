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

    public void setClicked(boolean clicked) {
        if (!clicked && afterClick != null)
            afterClick.run();
        this.clicked = clicked;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (clicked) return;

        text.getColor().r = 0;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) return;
        switchMenu.setClicked(this);
        clicked = true;
        if (onClick != null) onClick.run();
        text.getColor().r = 255;
    }
}
