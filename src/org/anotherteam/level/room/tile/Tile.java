package org.anotherteam.level.room.tile;

import lombok.val;
import org.anotherteam.render.texture.Pixmap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Tile {
    public final static Vector2i SIZE = new Vector2i(16, 16);

    private final Pixmap ownerPixmap;
    private final int u, v, textureAreaSize;

    public Tile(int u, int v, int textureAreaSize, @NotNull Pixmap ownerPixmap) {
        this.ownerPixmap = ownerPixmap;
        this.u = u;
        this.v = v;
        this.textureAreaSize = textureAreaSize;
    }

    public Pixmap getTexturePixmap() {
        val result = new Pixmap(SIZE.x, SIZE.y);
        result.drawPixmap(ownerPixmap, 0, 0, u * SIZE.x, v * SIZE.y, SIZE.x, SIZE.y);
        return result;
    }

    public Pixmap getHeightPixmap() {
        val result = new Pixmap(SIZE.x, SIZE.y);
        result.drawPixmap(ownerPixmap, 0, 0, u * SIZE.x, textureAreaSize + v * SIZE.y, SIZE.x, SIZE.y);
        return result;
    }
}
