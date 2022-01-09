package org.anotherteam.editor.gui;

import org.anotherteam.util.Color;

public abstract class Button extends GUIElement {

    protected boolean clicked;
    protected Runnable onClick;

    protected boolean lock;

    public Button(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        setColor(Color.voidColor());
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setOnClick(Runnable runnable) {
        this.onClick = runnable;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public abstract void update(float dt);

    public boolean isClicked() {
        return clicked;
    }
}
