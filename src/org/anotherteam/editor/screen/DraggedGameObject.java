package org.anotherteam.editor.screen;

import org.anotherteam.object.GameObject;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.DraggedThing;
import org.jetbrains.annotations.NotNull;

public final class DraggedGameObject extends DraggedThing {

    private final GameObject gameObject;

    public DraggedGameObject(@NotNull Sprite sprite, @NotNull GameObject gameObject) {
        super(sprite);
        this.gameObject = gameObject;
    }

    @NotNull
    public GameObject getGameObject() {
        return gameObject;
    }
}
