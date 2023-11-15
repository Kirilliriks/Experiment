package org.anotherteam.widget;

import imgui.ImGui;
import org.anotherteam.input.Input;
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
        this.position = new Vector2i(x, y);
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

    public boolean isClicked() {
        return ImGui.isWindowHovered() && ImGui.isMouseClicked(Input.MOUSE_LEFT_BUTTON.getButtonCode()) && !ImGui.isWindowAppearing();
    }

    public void reset() {
        dirty = true;

        position.x = xInit;
        position.y = yInit;
        width = widthInit;
        height = heightInit;
    }
}
