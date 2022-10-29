package org.anotherteam.screen;

import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

public abstract class DraggedThing {

    public abstract void draw(int x, int y, @NotNull RenderBatch renderBatch);

    public abstract void debugDraw(int x, int y, boolean inEditor, @NotNull RenderBatch renderBatch);
}
