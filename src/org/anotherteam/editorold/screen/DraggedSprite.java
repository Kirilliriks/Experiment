package org.anotherteam.editorold.screen;

import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.DraggedThing;
import org.jetbrains.annotations.NotNull;

public abstract class DraggedSprite extends DraggedThing {

    protected final Sprite sprite;
    protected final int frameX, frameY;

    public DraggedSprite(@NotNull Sprite sprite) {
        this.sprite = sprite;
        frameX = sprite.getFrameX();
        frameY = sprite.getFrameY();
    }

    @NotNull
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
