package org.anotherteam.editor.gui;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public class Label extends GUIElement {

    public static final int DEFAULT_HEIGHT = 16;
    public static final int DEFAULT_TEXT_OFFSET = 6;

    protected String text;

    public Label(String text, float x, float y, GUIElement ownerElement) {
        super(x, y, 0, DEFAULT_HEIGHT, ownerElement);
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + DEFAULT_TEXT_OFFSET * 2;
        setColor(Color.GRAY);
    }

    public void setText(String text) {
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + DEFAULT_TEXT_OFFSET * 2;
    }

    public String getText() {
        return text;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        editorBatch.drawText(Editor.editorFont, text, (int) (getPosX() + DEFAULT_TEXT_OFFSET / 2), (int)(getPosY() + DEFAULT_TEXT_OFFSET / 2), 1.0f, Color.BLACK);
    }
}
