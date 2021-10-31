package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.util.Color;

public abstract class Button extends GUIElement {

    public static final Color DEFAULT_BUTTON_COLOR = new Color(0, 100, 100, 255);

    protected boolean clicked;
    protected Runnable onClick;

    public Button(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        setColor(DEFAULT_BUTTON_COLOR);
    }

    public void setOnClick(Runnable runnable) {
        this.onClick = runnable;
    }

    @Override
    public abstract void update(float dt);

    public boolean mouseOnWidget() {
        val mouseX = Input.getMousePos().x;
        val mouseY = Input.getMousePos().y;
        if (mouseX < getPosX() || mouseX > getPosX() + width) return false;
        return (!(mouseY < getPosY() || mouseY > getPosY() + height));
    }

    public boolean isClicked() {
        return clicked;
    }
}
