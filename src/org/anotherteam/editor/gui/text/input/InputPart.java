package org.anotherteam.editor.gui.text.input;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.util.Color;
import org.anotherteam.util.StringUtil;

public final class InputPart extends TextButton {

    public static final Color DEFAULT_COLOR = new Color(180, 180, 180);

    private String lastCorrectInput;
    private Runnable onUnFocus;
    private Type type;

    public InputPart(String text, float x, float y, GUIElement ownerElement) {
        super(text, x, y, ownerElement);
        setLabelText(text);
        onUnFocus = null;
        type = Type.STRING;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setOnUnFocus(Runnable onUnFocus) {
        this.onUnFocus = onUnFocus;
    }

    @Override
    public void setLabelText(String text) {
        super.setLabelText(text);
        lastCorrectInput = text;
    }

    @Override
    public void setClicked(boolean clicked) {
        if (lock) {
            clicked = false;
        }
        this.clicked = clicked;

        if (!clicked) return;
        timeToRelease = releaseTime;
        if (onClick != null) onClick.run();
    }

    @Override
    public boolean isMouseOnWidget() {
        if (super.isMouseOnWidget()) {
            if (lock) {
                labelText.getColor().set(100, 100, 100);
            } else {
                labelText.getColor().set(220, 220, 220);
            }
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
                Editor.inputHandling = false;
                clicked = false;
                if (onUnFocus != null) onUnFocus.run();
            }
            return;
        }
        if (!isMouseOnWidget()) return;
        if (!Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) return;
        Editor.inputHandling = true;
        setClicked(true);
    }

    public void handleInput() {
        if (!Input.isAnyKeyDown()) return;
        val key = Input.getLastPrintedKey();
        if (key == null) return;

        final String text = labelText.getText();
        if (key == Input.KEY_BACKSPACE) {
            if (text.length() == 0) return;
            labelText.setText(text.substring(0, text.length() - 1));
            return;
        }
        if (!key.isPrintable()) return;

        final String newText = text + key.getChar();
        if (!validateInput(newText)) return;

        labelText.setText(newText);
    }

    private boolean validateInput(String text) {
        if (type == Type.INTEGER) {
            if (!StringUtil.isNumeric(text)) return false;
        }
        return true;
    }

    public enum Type {
        STRING,
        INTEGER
    }
}
