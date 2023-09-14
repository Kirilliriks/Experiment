package org.anotherteam.widget;

import imgui.ImGui;
import org.joml.Vector2i;

public abstract class Widget {

    protected final int xInit, yInit;
    protected final int widthInit, heightInit;

    protected boolean dirty;
    protected Vector2i position;
    protected int width, height;

    public Widget(int x, int y, int width, int height) {
        this(false, x, y, width, height);
    }

    public Widget(boolean dirty, int x, int y, int width, int height) {
        this.dirty = dirty;
        this.xInit = x;
        this.yInit = y;
        this.position = new Vector2i(xInit, yInit);
        this.width = widthInit = width;
        this.height = heightInit = height;
    }

    protected void onDirty() {
        if (dirty) {
            ImGui.setNextWindowPos(position.x, position.y);
            ImGui.setNextWindowSize(width, height);

            dirty = false;
        }
    }

    public void update() {
        onDirty();
    }

    public void reset() {
        dirty = true;

        position.x = xInit;
        position.y = yInit;
        width = widthInit;
        height = heightInit;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setX(int x) {
        dirty = true;
        position.x = x;
    }

    public int getX() {
        return position.x;
    }

    public void setY(int y) {
        dirty = true;
        position.y = y;
    }

    public int getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        dirty = true;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        dirty = true;
        this.height = height;
    }
}
