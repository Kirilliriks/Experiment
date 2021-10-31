package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.util.Color;
import org.lwjgl.glfw.GLFW;

public class TextButton extends Button {

    protected float releaseTime;
    protected float timeToRelease;
    protected final Label text;

    public TextButton(String text, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.text = new Label(text, 0, 0, this);
        this.text.setColor(DEFAULT_BUTTON_COLOR);
        releaseTime = 0.6f;
        timeToRelease = 0.0f;
        setColor(Color.VOID);
        width = this.text.getWidth();
        height = this.text.getHeight();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        text.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        text.setHeight(height);
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (timeToRelease > 0.0f) {
            timeToRelease -=dt;
            return;
        }
        clicked = false;
        text.getColor().r = 0;
        if (!mouseOnWidget()) return;
        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) return;
        timeToRelease = releaseTime;
        clicked = true;
        if (onClick != null) onClick.run();
        text.getColor().r = 255;
    }

    public String getText() {
        return text.getText();
    }
}
