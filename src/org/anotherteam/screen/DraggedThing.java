package org.anotherteam.screen;

import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

public abstract class DraggedThing {

    private final Sprite sprite;

    public DraggedThing(@NotNull Sprite sprite) {
        this.sprite = sprite;
    }

    @NotNull
    public Sprite getSprite() {
        return sprite;
    }
}
