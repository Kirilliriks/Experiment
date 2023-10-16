package org.anotherteam.dragged;

import lombok.Getter;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.screen.DraggedThing;

@Getter
public final class DraggedGameObject extends DraggedThing {

    private final GameObject gameObject;

    public DraggedGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void draw(int x, int y, RenderBatch renderBatch) {
        gameObject.getTransform().setPosition(x, y);
        gameObject.draw(renderBatch, false);
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, RenderBatch renderBatch) {
        gameObject.debugDraw(renderBatch, false);
    }
}
