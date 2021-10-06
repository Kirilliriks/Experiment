package org.anotherteam.math;

import lombok.NonNull;

public final class Vector2i {

    public int x, y;

    public Vector2i(@NonNull Vector2i vector2i) {
        this(vector2i.x, vector2i.y);
    }

    public Vector2i() {
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(@NonNull Vector2i vector2i) {
        this.x = vector2i.x;
        this.y = vector2i.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2i add(@NonNull Vector2i vector2i) {
        this.x += vector2i.x;
        this.y += vector2i.y;
        return this;
    }

    public Vector2i copy() {
        return new Vector2i(this);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public void setZero() {
        x = 0;
        y = 0;
    }
}
