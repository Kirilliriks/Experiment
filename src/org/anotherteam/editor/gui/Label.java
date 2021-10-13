package org.anotherteam.editor.gui;

import org.anotherteam.editor.Editor;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class Label extends GUIElement {

    private String text;
    private Color color;

    public Label(String text, @NotNull Vector2f pos, @NotNull Vector2f offset, int width, int height) {
        super(pos, width, height);
        this.text = text;
        this.pos = pos;
        this.width = width;
        this.height = height;
        color = Color.WHITE;
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        super.render(editorBatch);
        editorBatch.drawText(Editor.editorFont, text, (int) (pos.x + offset.x), (int)(pos.y + offset.y), 1.0f, Color.BLACK);
    }
}
