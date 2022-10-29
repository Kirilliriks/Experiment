package org.anotherteam.oldeditor.screen;

import org.anotherteam.object.GameObject;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

public final class DraggedGameObject extends DraggedSprite {

    private final GameObject gameObject;

    public DraggedGameObject(@NotNull Sprite sprite, @NotNull GameObject gameObject) {
        super(sprite);
        this.gameObject = gameObject;
        gameObject.prepare();
    }

    @NotNull
    public GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public void draw(int x, int y, @NotNull RenderBatch renderBatch) {
        gameObject.setPosition(x, y);
        gameObject.draw(renderBatch, false);

    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, @NotNull RenderBatch renderBatch) {
        gameObject.debugDraw(renderBatch, inEditor);
    }
}
