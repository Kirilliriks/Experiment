package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.util.Color;
import org.lwjgl.glfw.GLFW;

public class TextButton extends Button {

    public static final Color DEFAULT_TEXT_BUTTON_COLOR = new Color(0, 100, 100, 255);

    protected float releaseTime;
    protected float timeToRelease;
    protected final Label text;

    public TextButton(String text, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        this.text = new Label(text, 0, 0, this);
        this.text.setColor(DEFAULT_TEXT_BUTTON_COLOR);
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
    public void setClicked(boolean clicked) {
        super.setClicked(clicked);
        if (!clicked) {
            text.getColor().r = 0;
            return;
        }
        timeToRelease = releaseTime;
        if (onClick != null) onClick.run();
        text.getColor().r = 255;
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (timeToRelease > 0.0f) {
            timeToRelease -=dt;
            return;
        }
        setClicked(false);
        if (!mouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        setClicked(true);
    }

    public String getText() {
        return text.getText();
    }
}
