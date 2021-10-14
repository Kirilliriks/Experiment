package org.anotherteam.level.room.tile;

import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Tile {
    public final static Vector2i SIZE = new Vector2i(16, 16);

    private final Sprite textureSprite;
    private final Sprite heightSprite;

    private final Vector2i position;

    public Tile(int x, int y, int frameX, int frameY, int heightOffset, @NotNull SpriteAtlas atlas) {
        this.position = new Vector2i(x, y);
        textureSprite = atlas.getSprite(frameX, frameY);
        heightSprite = atlas.getSprite(frameX, frameY + heightOffset);
    }

    @NotNull
    public Vector2i getPosition() {
        return position;
    }

    @NotNull
    public Sprite getTextureSprite() {
        return textureSprite;
    }

    @NotNull
    public Sprite getHeightSprite() {
        return heightSprite;
    }
}
