package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.EditorLog;
import org.anotherteam.editor.gui.GUIElement;

public class SwitchButton extends TextButton {

    private final SwitchMenu switchMenu;

    public SwitchButton(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        clicked = false;
        switchMenu = (SwitchMenu) ownerElement;
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

        this.clicked = clicked;

        if (!clicked) {
            labelText.getColor().set(DEFAULT_COLOR);
            return;
        }

        if (onClick != null) runClick(new ClickInfo(left));

        labelText.getColor().set(DEFAULT_COLOR);
        labelText.getColor().r = 255;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (!isMouseOnWidget()) return;

        final boolean left = Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON);
        final boolean right = Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON);
        if (!left && !right) return;

        switchMenu.setClicked(this, left);
    }
}
