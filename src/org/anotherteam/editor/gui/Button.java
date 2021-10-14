package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Button extends Label {

    private float releaseTime;
    private float timeToRelease;
    private boolean clicked;
    private Runnable runnable;

    public Button(String text, @NotNull Vector2f offset, int height) {
        super(text, offset, height);
        releaseTime = 1.0f;
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
        Editor.setLogText("NULL");
        val mouseX = Input.getMousePos().x;
        val mouseY = Input.getMousePos().y;
        if (mouseX < pos.x + offset.x || mouseX > pos.x + offset.x + width) return false;
        if (mouseY < pos.y + offset.y || mouseY > pos.y + offset.y + height) return false;
        Editor.setLogText("Mouse!");
        return true;
    }

    public boolean isClicked() {
        return clicked;
    }
}
