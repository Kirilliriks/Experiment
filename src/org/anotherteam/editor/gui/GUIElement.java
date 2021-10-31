package org.anotherteam.editor.gui;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIElement {

    public static final Color DEFAULT_COLOR = new Color(150, 150, 150, 255);

    protected final List<GUIElement> childElements;
    protected final GUIElement ownerElement;
    protected final Vector2f pos; // If (ownerElement != null) pos work's like offset
    protected int width, height;
    protected final Color color;

    protected boolean inverted;
    protected boolean visible;

    public GUIElement(float x, float y, GUIElement ownerElement) {
        this(x, y, 0, 0, ownerElement);
    }

    public GUIElement(float x, float y, int width, int height, GUIElement ownerElement) {
        this.pos = new Vector2f(x, y);
        this.width = width;
        this.height = height;
        this.color = new Color(DEFAULT_COLOR);
        visible = true;
        inverted = false;
        childElements = new ArrayList<>();

        this.ownerElement = ownerElement;
        if (ownerElement == null) return;
        ownerElement.childElements.add(this);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setPosX(float x) {
        pos.x = x;
    }

    public void setPosY(float y) {
        pos.y = y;
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColor(int r, int g, int b, int a) {
        color.r = r;
        color.g = g;
        color.b = b;
        color.a = a;
    }

    public void setColor(Color color) {
        this.color.r = color.r;
        this.color.g = color.g;
        this.color.b = color.b;
        this.color.a = color.a;
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    public float getPosX() {
        if (ownerElement == null) return pos.x;
        return ownerElement.getPosX() + pos.x;
    }

    public float getPosY() {
        if (ownerElement == null) return pos.y;
        return ownerElement.getPosY() + pos.y + (inverted ? (-height + ownerElement.height) : 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    public List<GUIElement> getChildElements() {
        return childElements;
    }

    public void update(float dt) { }

    public void updateElements(float dt) {
        if (!visible) return;

        for (val element : childElements) {
            if (!element.visible) continue;
            element.update(dt);
            element.updateElements(dt);
        }
    }

    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        if (width > 0 || height > 0)
            editorBatch.draw(AssetData.EDITOR_TEXTURE, getPosX(), getPosY(), width, height, false, false, color);

        for (val element : childElements) {
            element.render(editorBatch);
        }
    }
}
