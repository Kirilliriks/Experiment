package org.anotherteam.screen;

import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;

public abstract class DraggedThing {

    public abstract void render(int x, int y, @NotNull RenderBatch renderBatch);
}
