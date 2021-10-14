package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.data.AssetsData;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIElement {

    protected final List<GUIElement> childElements;
    protected GUIElement ownerElement;
    protected Vector2f pos; // If (ownerElement != null) pos work's like offset
    protected int width, height;
    protected Color color;

    protected boolean visible;

    public GUIElement(int width, int height) {
        this.width = width;
        this.height = height;
        visible = true;
        childElements = new ArrayList<>();
    }

    public GUIElement(float x, float y, Color color) {
        this(x, y, 0, 0, color);
    }

    public GUIElement(float x, float y, int width, int height) {
        this(x, y, width, height, Color.GRAY);
    }

    public GUIElement(float x, float y, int width, int height, Color color) {
        this.pos = new Vector2f(x, y);
        this.width = width;
        this.height = height;
        this.color = color;
        visible = true;
        childElements = new ArrayList<>();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setPos(float x, float y) {
        this.pos = new Vector2f(x, y);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getPosX() {
        if (ownerElement == null) return pos.x;
        return pos.x + ownerElement.getPosX();
    }

    public float getPosY() {
        if (ownerElement == null) return pos.y;
        return pos.y + ownerElement.getPosY();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOwner(@NotNull GUIElement ownerElement) {
        this.ownerElement = ownerElement;
    }

    public void addElement(@NotNull GUIElement element) {
        childElements.add(element);
        element.setOwner(this);
    }

    public void update(float dt) {
        if (!visible) return;

        for (val element : childElements) {
            if (!element.visible) continue;
            element.update(dt);
        }
    }

    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        editorBatch.draw(AssetsData.EDITOR_TEXTURE, getPosX(), getPosY(), width, height, false, false, color);
        for (val element : childElements) {
            element.render(editorBatch);
        }
    }
}
