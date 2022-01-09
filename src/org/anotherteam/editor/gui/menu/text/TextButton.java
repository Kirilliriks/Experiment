package org.anotherteam.editor.gui.menu.text;

import org.anotherteam.Input;
import org.anotherteam.editor.gui.Button;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.util.Color;

public class TextButton extends Button {

    public static final Color DEFAULT_COLOR = new Color(0, 100, 100);

    protected final Label labelText;

    public TextButton(String text, float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        labelText = new Label(text, 0, 0, this);
        labelText.setColor(DEFAULT_COLOR);
        width = labelText.getWidth();
        height = labelText.getHeight();
        setColor(Color.VOID);
    }

    public void setLabelText(String text) {
        labelText.setText(text);
        width = labelText.getWidth();
    }

    public String getLabelText() {
        return labelText.getText();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        labelText.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        labelText.setHeight(height);
    }

    @Override
    public void setClicked(boolean clicked) {
        if (lock) {
            clicked = false;
        }
        super.setClicked(clicked);

        if (!clicked) {
            labelText.getColor().set(DEFAULT_COLOR);
            return;
        }
        timeToRelease = releaseTime;
        if (onClick != null) onClick.run();
        labelText.getColor().set(DEFAULT_COLOR);
        labelText.getColor().r = 255;
    }

    @Override
    public boolean isMouseOnWidget() {
        if (super.isMouseOnWidget()) {
            if (lock) {
                labelText.getColor().set(100, 0, 0);
            } else {
                labelText.getColor().g = 200;
            }
            return true;
        } else {
            labelText.getColor().set(DEFAULT_COLOR);
            return false;
        }
    }

    @Override
    public void update(float dt) {
        if (!visible) return;

        if (timeToRelease > 0.0f) {
            timeToRelease -=dt;
            return;
        }
        setClicked(false);
        if (!isMouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        setClicked(true);
    }
}
