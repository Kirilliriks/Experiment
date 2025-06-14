package org.anotherteam.editor.gui;

import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIElement {

    public static final Color DEFAULT_COLOR = new Color(150, 150, 150);

    protected final List<GUIElement> childElements;
    protected final GUIElement ownerElement;
    protected final Vector2f pos; // If (ownerElement != null) pos work's like offset
    protected final Vector2f offset; // If (ownerElement != null) pos work's like offset
    protected int width, height;
    protected final Color color;

    protected boolean inverted;
    protected boolean visible;

    public GUIElement(float x, float y, GUIElement ownerElement) {
        this(x, y, 0, 0, ownerElement);
    }

    public GUIElement(float x, float y, int width, int height, GUIElement ownerElement) {
        this.pos = new Vector2f(x, y);
        this.offset = new Vector2f();
        this.width = width;
        this.height = height;
        this.color = new Color(DEFAULT_COLOR);
        visible = true;
        inverted = false;
        childElements = new ArrayList<>();

        this.ownerElement = ownerElement;

        if (ownerElement != null) {
            ownerElement.childElements.add(this);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        GameScreen.draggedThing = null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setInvertedY(boolean inverted) {
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

    public void setColor(int r, int g, int b) {
        color.set(r, g, b);
    }

    public void setColor(int r, int g, int b, int a) {
        color.set(r, g, b, a);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    @NotNull
    public Color getColor() {
        return color;
    }

    public float getPosX() {
        if (ownerElement == null) return pos.x + offset.x;
        return ownerElement.getPosX() + pos.x + offset.x;
    }

    public float getPosY() {
        if (ownerElement == null) return pos.y + offset.y;
        return ownerElement.getPosY() + pos.y + offset.y + (inverted ? (-height + ownerElement.height) : 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMouseOnWidget() {
        final var mouseX = Input.getMousePos().x;
        if (mouseX < getPosX() || mouseX > getPosX() + width) return false;

        final var mouseY = Input.getMousePos().y;
        return (!(mouseY < getPosY() || mouseY > getPosY() + height));
    }

    public void update(float dt) { }

    public void updateElements(float dt) {
        if (!visible) return;

        for (final var element : childElements) {
            if (!element.visible) continue;
            element.update(dt);
            element.updateElements(dt);
        }
    }

    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        if (width > 0 || height > 0) {
            editorBatch.draw(AssetData.EDITOR_TEXTURE, getPosX(), getPosY(), width, height, false, false, color);
        }

        for (final var element : childElements) {
            element.render(editorBatch);
        }
    }

    public void removeChild(GUIElement element) {
        childElements.remove(element);
        element.destroy();
    }

    public void clearChild() {
        childElements.clear();
    }

    public void destroy() {
        if (childElements.isEmpty()) return;

        for (final var element : childElements) {
            element.destroy();
        }
    }
}
