package org.anotherteam.editor.gui;

import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.render.EditorBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public abstract class GUIElement {

    protected final Vector2f offset;
    protected Vector2f pos;
    protected int width, height;

    public GUIElement(int width, int height) {
        this(new Vector2f(0, 0), width, height);
    }

    public GUIElement(Vector2f pos, int width, int height) {
        this.offset = new Vector2f();
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public void setOffset(float x, float y) {
        offset.set(x, y);
    }

    public void render(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(AssetsData.EDITOR_TEXTURE, pos.x + offset.x, pos.y + offset.y, width, height, false, false);
    }
}
