package org.anotherteam.dragged;

import lombok.Getter;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.screen.DraggedThing;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public void placeTiles(Room room, int x, int y) {
        for (final var tile : tiles) {
            final int xFinal = x + tile.getFrameX() - x0;
            final int yFinal = y + tile.getFrameY() - y0;
            if (xFinal < 0 || yFinal < 0) {
                continue;
            }

            room.setTile(new Tile(xFinal, yFinal, tile.getFrameX(), tile.getFrameY(), atlas));
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
            final int xFinal = x + (tile.getFrameX() - x0) * Tile.SIZE.x;
            final int yFinal = y + (tile.getFrameY() - y0) * Tile.SIZE.y;
            if (xFinal < 0 || yFinal < 0) {
                continue;
            }

            tile.draw(xFinal, yFinal, renderBatch);
        }
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, RenderBatch renderBatch) {
        for (final var tile : tiles) {
            final int xFinal = x + (tile.getFrameX() - x0) * Tile.SIZE.x;
            final int yFinal = y + (tile.getFrameY() - y0) * Tile.SIZE.y;
            if (xFinal < 0 || yFinal < 0) {
                continue;
            }

            tile.debugDraw(xFinal, yFinal, inEditor, renderBatch);
        }
    }
}
