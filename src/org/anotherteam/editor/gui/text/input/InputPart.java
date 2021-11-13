package org.anotherteam.editor.gui.text.input;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.util.CharUtil;
import org.anotherteam.util.Color;
import org.lwjgl.glfw.GLFW;

public final class InputPart extends TextButton {

    public static final Color DEFAULT_COLOR = new Color(180, 180, 180);

    public InputPart(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
    }

    @Override
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
        if (!clicked) return;
        timeToRelease = releaseTime;
        if (onClick != null) onClick.run();
    }

    @Override
    public boolean isMouseOnWidget() {
        if (super.isMouseOnWidget()) {
            labelText.getColor().set(220, 220, 220);
            return true;
        } else {
            labelText.getColor().set(DEFAULT_COLOR);
            return false;
        }
    }

    @Override
    public void update(float dt) {
        if (clicked) {
            handleInput();
            if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
            if (!isMouseOnWidget()) {
                clicked = false;
            }
            return;
        }
        if (!isMouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        setClicked(true);
    }

    public void handleInput() {
        if (!Input.isAnyKeyDown()) return;
        val key = Input.getLastPrintedKey();
        if (key == null) return;

        val text = labelText.getText();
        if (key == Input.KEY_BACKSPACE) {
            if (text.length() == 0) return;
            labelText.setText(text.substring(0, text.length() - 1));
            return;
        }
        if (!key.isPrintable()) return;

        labelText.setText(text + key.getChar());
    }
}
