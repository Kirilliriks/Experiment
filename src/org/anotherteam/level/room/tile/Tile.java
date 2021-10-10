package org.anotherteam.level.room.tile;

import org.anotherteam.render.texture.Pixmap;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Tile {
    public final static Vector2i SIZE = new Vector2i(16, 16);

    private final Pixmap texturePixmap;
    private final Pixmap heightPixmap;

    public Tile(int u, int v, int heightOffset, @NotNull Pixmap ownerPixmap) {
        texturePixmap = new Pixmap(SIZE.x, SIZE.y);
        texturePixmap.drawPixmap(ownerPixmap, 0, 0, u * SIZE.x, v * SIZE.y, SIZE.x, SIZE.y);
        heightPixmap = new Pixmap(SIZE.x, SIZE.y);
        heightPixmap.drawPixmap(ownerPixmap, 0, 0, u * SIZE.x, heightOffset + v * SIZE.y, SIZE.x, SIZE.y);
    }

    public Pixmap getTexturePixmap() {
        return texturePixmap;
    }

    public Pixmap getHeightPixmap() {
        return heightPixmap;
    }
}
