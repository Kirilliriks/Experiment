package org.anotherteam.editor.gui;

import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public abstract class GUIElement {

    protected final Vector2f offset;
    protected Vector2f pos;
    protected int width, height;
    protected Color color;

    public GUIElement(int width, int height) {
        this.offset = new Vector2f(0, 0);
        this.width = width;
        this.height = height;
    }

    public GUIElement(Vector2f pos, int width, int height) {
        this(pos, 0, 0, width, height, Color.WHITE);
    }

    public GUIElement(@NotNull Vector2f pos, float offsetX, float offsetY, int width, int height, Color color) {
        this.offset = new Vector2f(offsetX, offsetY);
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setOwner(@NotNull GUIElement ownerElement) {
        pos = ownerElement.pos;
    }

    public void setOffset(float x, float y) {
        offset.set(x, y);
    }

    public void render(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(AssetsData.EDITOR_TEXTURE, pos.x + offset.x, pos.y + offset.y, width, height, false, false, color);
    }

    public void update(float dt) { }
}
