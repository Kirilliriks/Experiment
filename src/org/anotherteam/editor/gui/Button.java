package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Button extends Label {

    private boolean clicked;
    private Runnable runnable;

    public Button(String text, @NotNull Vector2f offset, int height) {
        super(text, offset, height);
        color = new Color(0, 100, 100, 255);
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void update(float dt) {
        clicked = false;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) return;
        clicked = true;
        if (runnable != null) runnable.run();
        color.r += 1;
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
}
