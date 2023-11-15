package org.anotherteam.dragged;

import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.DraggedObject;

public abstract class DraggedSprite extends DraggedObject {

    protected final Sprite sprite;

    public DraggedSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getFrameX() {
        return sprite.getFrameX();
    }

    public int getFrameY() {
        return sprite.getFrameY();
    }
}
