package org.anotherteam.level.room.tile;

import org.anotherteam.data.AssetData;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Tile {
    public final static Vector2i SIZE = new Vector2i(16, 16);

    private final Sprite textureSprite;
    private final Sprite heightSprite;

    private final Vector2i position;

    public Tile(int x, int y, int frameX, int frameY, String atlasName) {
        this(x, y, frameX, frameY, AssetData.getSpriteAtlas(atlasName));
    }

    public Tile(int x, int y, @NotNull SpriteAtlas atlas) {
        this(x, y, x, y, atlas);
    }

    public Tile(int x, int y, int frameX, int frameY, @NotNull SpriteAtlas atlas) {
        this.position = new Vector2i(x, y);
        textureSprite = atlas.getSprite(frameX, frameY);
        heightSprite = atlas.getSprite(frameX, frameY + atlas.getHeightOffset());
    }

    public int getFrameX() {
        return textureSprite.getFrameX();
    }

    public int getFrameY() {
        return textureSprite.getFrameY();
    }

    public String getAtlasName() {
        return textureSprite.getTexture().getName();
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
