package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.util.Color;
import org.lwjgl.glfw.GLFW;

public class Button extends Label {

    protected float releaseTime;
    protected float timeToRelease;
    protected boolean clicked;
    protected Runnable runnable;

    public Button(String text, float x, float y) {
        super(text, x, y);
        releaseTime = 0.6f;
        timeToRelease = 0.0f;
        color = new Color(0, 100, 100, 255);
    }

    public void setReleaseTime(float releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void update(float dt) {
        if (timeToRelease > 0.0f) {
            timeToRelease -=dt;
            return;
        }
        clicked = false;
        color.r = 0;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) return;
        timeToRelease = releaseTime;
        clicked = true;
        if (runnable != null) runnable.run();
        color.r = 255;
    }

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
