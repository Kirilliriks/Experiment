package org.anotherteam.editor.screen;

import org.anotherteam.object.GameObject;
import org.anotherteam.render.batch.RenderBatch;
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

    @Override
    public void render(int x, int y, @NotNull RenderBatch renderBatch) {
        gameObject.setPosition(x, y);
        gameObject.render(renderBatch);
    }
}
