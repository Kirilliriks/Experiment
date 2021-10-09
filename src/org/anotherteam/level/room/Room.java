package org.anotherteam.level.room;

import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Vector;

public final class Room {

    private final Vector<Tile> tiles;
    private final Texture roomTexture;
    private final Texture heightTexture;

    private final Vector2i position;
    private final Vector2i size;

    public Room(@NotNull Vector2i position, @NotNull Vector2i size) {
        this.position = position;
        this.size = size;
        this.tiles = new Vector<>();
        tiles.setSize(size.x * size.y);
        this.roomTexture = new Texture(size.x * Tile.SIZE.x, size.y * Tile.SIZE.y);
        this.heightTexture = new Texture(size.x * Tile.SIZE.x, size.y * Tile.SIZE.y);
    }

    public void drawTexture(@NotNull RenderBatch renderBatch) {
        renderBatch.draw(roomTexture, position);
    }

    public void drawHeight(@NotNull RenderBatch renderBatch) {
        renderBatch.draw(heightTexture, position);
    }

    public void addTile(int x, int y, @NotNull Tile tile) {
        tiles.add(x + y * size.x, tile);
        roomTexture.drawPixmap(tile.getTexturePixmap(), x * Tile.SIZE.x, y * Tile.SIZE.y);
        heightTexture.drawPixmap(tile.getHeightPixmap(), x * Tile.SIZE.x, y * Tile.SIZE.y);
    }

    public void destroy() {
        roomTexture.destroy();
        heightTexture.destroy();
    }
}
