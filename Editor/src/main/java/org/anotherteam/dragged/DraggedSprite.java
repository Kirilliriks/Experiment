package org.anotherteam.dragged;

import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.DraggedThing;

public abstract class DraggedSprite extends DraggedThing {

    protected final Sprite sprite;
    protected final int frameX, frameY;

    public DraggedSprite(Sprite sprite) {
        this.sprite = sprite;
        frameX = sprite.getFrameX();
        frameY = sprite.getFrameY();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }
}
