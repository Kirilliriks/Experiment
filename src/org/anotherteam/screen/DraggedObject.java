package org.anotherteam.screen;

import org.anotherteam.render.batch.RenderBatch;

public abstract class DraggedObject {

    public abstract void draw(int x, int y, RenderBatch renderBatch);

    public abstract void debugDraw(int x, int y, boolean inEditor, RenderBatch renderBatch);
}
