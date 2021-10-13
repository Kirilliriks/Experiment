package org.anotherteam.editor.gui;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class Label extends GUIElement {

    private String text;

    public Label(String text, @NotNull Vector2f pos, @NotNull Vector2f offset, int width, int height) {
        super(pos, width, height);
        setOffset(offset.x, offset.y);
        this.text = text;
        this.pos = pos;
        this.width = Editor.editorFont.getTextWidth(text, 1.0f) + 5;
        this.height = height + 6;
        color = Color.GRAY;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        editorBatch.drawText(Editor.editorFont, text, (int) (pos.x + 2 + offset.x), (int)(pos.y + 3 + offset.y), 1.0f, Color.BLACK);
    }
}
