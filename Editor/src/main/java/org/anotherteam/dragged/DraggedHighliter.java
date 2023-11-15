package org.anotherteam.dragged;

import org.anotherteam.Editor;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.screen.DraggedObject;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;

public final class DraggedHighliter extends DraggedObject {

    private final Color color = new Color(255, 233, 127, 100);

    @Override
    public void draw(int x, int y, @NotNull RenderBatch renderBatch) {
        final int xF = (x / Tile.SIZE.x) * Tile.SIZE.x;
        final int yF = (y / Tile.SIZE.y) * Tile.SIZE.y;
        renderBatch.draw(Editor.EDITOR_HIGHLITER_TEXTURE, xF, yF, Tile.SIZE.x, Tile.SIZE.y, false, false, color);
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, @NotNull RenderBatch renderBatch) { }
}
