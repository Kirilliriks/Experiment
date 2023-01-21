package org.anotherteam.level.room.tile;

import org.anotherteam.data.AssetData;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class Tile {
    public final static Vector2i SIZE = new Vector2i(16, 16);

    private final Sprite textureSprite;
    private final Sprite heightSprite;

    private final Vector2i position;

    private boolean flipX, flipY;

    public Tile(int x, int y, int frameX, int frameY, String atlasName) {
        this(x, y, frameX, frameY, AssetData.getOrLoadRoomAtlas(atlasName));
    }

    public Tile(int x, int y, int frameX, int frameY, @NotNull SpriteAtlas atlas) {
        position = new Vector2i(x, y);
        textureSprite = atlas.getTextureSprite(frameX, frameY);
        heightSprite = atlas.getHeightSprite(frameX, frameY);
        flipX = false;
        flipY = false;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
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

    public void draw(@NotNull RenderBatch renderBatch, boolean height) {
        final var sprite = height ? heightSprite : textureSprite;
        renderBatch.draw(sprite, position.x * Tile.SIZE.x, position.y * Tile.SIZE.y, flipX, flipY);
    }
}
