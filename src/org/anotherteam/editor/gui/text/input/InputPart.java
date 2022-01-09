package org.anotherteam.editor.gui.text.input;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.menu.text.TextButton;
import org.anotherteam.editor.gui.text.Label;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.util.Color;
import org.anotherteam.util.StringUtil;

public final class InputPart extends TextButton {

    public static final Color INPUT_COLOR = new Color(180, 180, 180);

    private Runnable onUnFocus;
    private Type type;

    private final Label valueInput;

    public InputPart(String inputName, float x, float y, GUIElement ownerElement) {
        super(inputName, x, y, ownerElement);

        valueInput = new Label("INPUT HERE", 0, 0, this);
        valueInput.setPosX(width);
        valueInput.setColor(INPUT_COLOR);

        width = getLabelWidth() + valueInput.getWidth();

        onUnFocus = null;
        type = Type.STRING;
        setColor(Color.VOID);
    }

    public String getValue() {
        return valueInput.getText();
    }

    public void setValue(String text) {
        valueInput.setText(text);
        width = getLabelWidth() + valueInput.getWidth();
    }

    public void setType(Type type) {
        this.type = type;

        switch (type) {
            case BOOLEAN -> onClick = () -> {
                boolean value = !Boolean.parseBoolean(valueInput.getText());
                setValue(String.valueOf(value));
                setClicked(false);
            };
        }
    }

    public void setOnUnFocus(Runnable onUnFocus) {
        this.onUnFocus = onUnFocus;
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
                valueInput.getColor().set(100, 100, 100);
            } else {
                valueInput.getColor().set(220, 220, 220);
            }
            return true;
        } else {
            valueInput.getColor().set(INPUT_COLOR);
            return false;
        }
    }

    @Override
    public void update(float dt) {
        if (clicked) {
            if (type != Type.BOOLEAN) {
                handleInput();
            }

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

    private void handleInput() {
        if (!Input.isAnyKeyDown()) return;
        val key = Input.getLastPrintedKey();
        if (key == null) return;

        final String text = valueInput.getText();
        if (key == Input.KEY_BACKSPACE) {
            if (text.length() == 0) return;
            valueInput.setText(text.substring(0, text.length() - 1));
            width = getLabelWidth() + valueInput.getWidth();
            return;
        }
        if (!key.isPrintable()) return;

        final String newText = text + key.getChar();
        if (!validateInput(newText)) return;

        valueInput.setText(newText);
        width = getLabelWidth() + valueInput.getWidth();
    }

    private boolean validateInput(String text) {
        if (type == Type.INTEGER) {
            if (!StringUtil.isNumeric(text)) return false;
        }
        return true;
    }

    public void setAfterUnFocus(Runnable afterClick) {
        setOnUnFocus(afterClick);
    }

    public enum Type {
        STRING,
        BOOLEAN,
        INTEGER
    }
}
