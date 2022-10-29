package org.anotherteam.oldeditor.gui.menu.text;

import org.anotherteam.Input;

public class SwitchButton extends TextButton {

    private final SwitchMenu menu;

    public SwitchButton(String text, float x, float y, SwitchMenu ownerElement) {
        super(text, x, y, ownerElement);
        clicked = false;
        menu = ownerElement;
    }

    public void setHighlighted(){
        clicked = true;
        labelText.getColor().set(DEFAULT_COLOR);
        labelText.getColor().r = 255;
    }

    @Override
    public void setClicked(boolean clicked, boolean left) {
        if (!clicked && afterClick != null) {
            runAfterClick();
        }

        if (!clicked) {
            labelText.getColor().set(DEFAULT_COLOR);
        } else {
            if (onClick != null) runClick(new ClickInfo(left));

            labelText.getColor().set(DEFAULT_COLOR);
            labelText.getColor().r = 255;
        }

        this.clicked = clicked;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (!isMouseOnWidget()) return;

        final boolean left = Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON);
        final boolean right = Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON);
        if (!left && !right) return;

        menu.setClicked(this, left);
    }
}
