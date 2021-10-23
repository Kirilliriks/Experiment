package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.util.Color;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Button extends Label {

    protected float releaseTime;
    protected float timeToRelease;
    protected boolean clicked;
    protected Runnable onClick;

    public Button(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        releaseTime = 0.6f;
        timeToRelease = 0.0f;
        color = new Color(0, 100, 100, 255);
    }

    public void setReleaseTime(float releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setOnClick(Runnable runnable) {
        this.onClick = runnable;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

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
        if (onClick != null) onClick.run();
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
