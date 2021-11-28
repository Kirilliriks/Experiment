package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.GUIElement;

public class SwitchButton extends TextButton {

    private final SwitchMenu switchMenu;
    private Runnable afterClick;

    public SwitchButton(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        clicked = false;
        switchMenu = (SwitchMenu) ownerElement;
    }

    public void setAfterClick(Runnable afterClick) {
        this.afterClick = afterClick;
    }

    public void setHighlighted(){
        clicked = true;
        labelText.getColor().set(DEFAULT_COLOR);
        labelText.getColor().r = 255;
    }

    @Override
    public void setClicked(boolean clicked) {
        if (!clicked && afterClick != null)
            afterClick.run();
        this.clicked = clicked;

        if (!clicked) {
            labelText.getColor().set(DEFAULT_COLOR);
            return;
        }
        if (onClick != null) onClick.run();
        labelText.getColor().set(DEFAULT_COLOR);
        labelText.getColor().r = 255;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (clicked) return;
        if (!isMouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        switchMenu.setClicked(this);
    }
}
