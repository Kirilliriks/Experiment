package org.anotherteam.editor.gui;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public class Label extends GUIElement {

    public static final int DEFAULT_HEIGHT = 16;

    protected String text;

    public Label(String text, float x, float y, GUIElement ownerElement) {
        super(x, y, 0, DEFAULT_HEIGHT, ownerElement);
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + 5;
        color = Color.GRAY;
    }

    public void setText(String text) {
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + 5;
    }

    public String getText() {
        return text;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        editorBatch.drawText(Editor.editorFont, text, (int) (getPosX() + 2), (int)(getPosY() + 3), 1.0f, Color.BLACK);
    }
}
