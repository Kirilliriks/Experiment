package org.anotherteam.screen;

import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.jetbrains.annotations.NotNull;

public abstract class DraggedThing {

    protected final Sprite sprite;

    public DraggedThing(@NotNull Sprite sprite) {
        this.sprite = sprite;
    }

    @NotNull
    public Sprite getSprite() {
        return sprite;
    }

    public void render(int x, int y, @NotNull RenderBatch renderBatch) {
        renderBatch.draw(GameScreen.draggedThing.getSprite(), x, y);
    }
}
