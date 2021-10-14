package org.anotherteam.editor.gui;

import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public abstract class GUIElement {

    protected GUIElement ownerElement;
    protected Vector2f pos; // If (ownerElement != null) pos work's like offset
    protected int width, height;
    protected Color color;

    public GUIElement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public GUIElement(float x, float y, int width, int height) {
        this(x, y, width, height, Color.WHITE);
    }

    public GUIElement(float x, float y, int width, int height, Color color) {
        this.pos = new Vector2f(x, y);
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setPos(float x, float y) {
        this.pos = new Vector2f(x, y);
    }

    public float getPosX() {
        if (ownerElement == null) return pos.x;
        return pos.x + ownerElement.getPosX();
    }

    public float getPosY() {
        if (ownerElement == null) return pos.y;
        return pos.y + ownerElement.getPosY();
    }

    public void setOwner(@NotNull GUIElement ownerElement) {
        this.ownerElement = ownerElement;
    }

    public void render(@NotNull EditorBatch editorBatch) {
        editorBatch.draw(AssetsData.EDITOR_TEXTURE, getPosX(), getPosY(), width, height, false, false, color);
    }

    public void update(float dt) { }
}
