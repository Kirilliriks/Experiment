package org.anotherteam.level.room;

import lombok.val;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;


public final class Room {

    private final Tile[] tiles;

    private final Vector2i position;
    private final Vector2i size;

    public Room(@NotNull Vector2i position, @NotNull Vector2i size) {
        this.position = position;
        this.size = size;
        this.tiles = new Tile[size.x * size.y];
    }

    public void drawTexture(@NotNull RenderBatch renderBatch) {
        for (val tile : tiles) {
            renderBatch.draw(tile.getTextureSprite(), position.x + tile.getPosition().x * Tile.SIZE.x, position.y + tile.getPosition().y * Tile.SIZE.y);
        }
    }

    public void drawHeight(@NotNull RenderBatch renderBatch) {
        int i = 0;
        for (val tile : tiles) {
            renderBatch.draw(tile.getHeightSprite(), position.x + tile.getPosition().x * Tile.SIZE.x, position.y + tile.getPosition().y * Tile.SIZE.y);
        }
    }

    public void addTile(int x, int y, @NotNull Tile tile) {
        tiles[x + y * size.x] =  tile;
    }

    public void destroy() { }
}
