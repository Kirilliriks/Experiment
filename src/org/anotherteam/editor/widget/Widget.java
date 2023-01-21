package org.anotherteam.editor.widget;

import imgui.ImGui;

public abstract class Widget {

    protected final int xInit, yInit;
    protected final int widthInit, heightInit;

    protected boolean dirty;
    protected int x, y;
    protected int width, height;

    public Widget(int x, int y, int width, int height) {
        this(false, x, y, width, height);
    }

    public Widget(boolean dirty, int x, int y, int width, int height) {
        this.dirty = dirty;
        this.x = xInit = x;
        this.y = yInit = y;
        this.width = widthInit = width;
        this.height = heightInit = height;
    }

    protected void onDirty() {
        if (dirty) {
            ImGui.setNextWindowPos(x, y);
            ImGui.setNextWindowSize(width, height);

            dirty = false;
        }
    }

    public abstract void update();

    public void reset() {
        dirty = true;

        x = xInit;
        y = yInit;
        width = widthInit;
        height = heightInit;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setX(int x) {
        dirty = true;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        dirty = true;
        this.y = y;
    }

    public int getY() {
        return y;
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
