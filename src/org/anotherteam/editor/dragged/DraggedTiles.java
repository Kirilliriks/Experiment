package org.anotherteam.editor.dragged;

import org.anotherteam.Game;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.screen.DraggedThing;

import java.util.ArrayList;
import java.util.List;

public final class DraggedTiles extends DraggedThing {

    private final SpriteAtlas atlas;

    private final int x0, y0;

    private final List<DraggedTile> tiles;

    public DraggedTiles(int x0, int y0, int x1, int y1, SpriteAtlas atlas) {
        this.atlas = atlas;
        this.x0 = x0;
        this.y0 = y0;

        tiles = new ArrayList<>();

        fillTiles(x0, y0, x1, y1);
    }

    public void placeTiles(int x, int y) {
        final Room room = Game.LEVEL_MANAGER.getCurrentRoom();

        for (final var tile : tiles) {
            final int xOffset = tile.frameX - x0;
            final int yOffset = tile.frameY - x0;

            room.setTile(new Tile(x + xOffset, y + yOffset, tile.frameX, tile.frameY, atlas));
        }
    }

    private void fillTiles(int x0, int y0, int x1, int y1) {
        final int xMin = Math.min(x0, x1);
        final int xMax = Math.max(x0, x1);
        final int yMin = Math.min(y0, y1);
        final int yMax = Math.max(y0, y1);

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {

                tiles.add(new DraggedTile(x, y, atlas));
            }
        }
    }


    @Override
    public void draw(int x, int y, RenderBatch renderBatch) {
        for (final var tile : tiles) {
            final int xOffset = (tile.frameX - x0) * Tile.SIZE.x;
            final int yOffset = (tile.frameY - y0) * Tile.SIZE.y;

            tile.draw(x + xOffset, y + yOffset, renderBatch);
        }
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor,  RenderBatch renderBatch) {
        for (final var tile : tiles) {
            final int xOffset = (tile.frameX - x0) * Tile.SIZE.x;
            final int yOffset = (tile.frameY - y0) * Tile.SIZE.y;

            tile.debugDraw(x + xOffset, y + yOffset, inEditor, renderBatch);
        }
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }
}
