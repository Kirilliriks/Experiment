package org.anotherteam.editor.gui;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class Label extends GUIElement {

    protected String text;

    public Label(String text, float x, float y, int height) {
        super(0, height + 6);
        this.pos = new Vector2f(x, y);
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + 5;
        color = Color.GRAY;
    }

    public void setText(String text) {
        this.text = text;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + 5;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);
        editorBatch.drawText(Editor.editorFont, text, (int) (getPosX() + 2), (int)(getPosY() + 3), 1.0f, Color.BLACK);
    }
}
