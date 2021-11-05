package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.util.Color;

public abstract class Button extends GUIElement {

    protected boolean clicked;
    protected Runnable onClick;

    public Button(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        setColor(Color.VOID);
    }

    public void setOnClick(Runnable runnable) {
        this.onClick = runnable;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public abstract void update(float dt);

    public boolean isMouseOnWidget() {
        val mouseX = Input.getMousePos().x;
        val mouseY = Input.getMousePos().y;
        if (mouseX < getPosX() || mouseX > getPosX() + width) return false;
        return (!(mouseY < getPosY() || mouseY > getPosY() + height));
    }

    public boolean isClicked() {
        return clicked;
    }
}
