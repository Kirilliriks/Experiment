package org.anotherteam.render.sprite;

import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SpriteAtlas {

    private final Texture texture;
    private final List<Sprite> sprites;

    private final int frameWidth, frameHeight;
    private final int sizeX, sizeY;

    private final int heightOffset;

    private SpriteAtlas(Texture texture, int frameWidth, int frameHeight, int heightOffset) {
        this.texture = texture;
        this.sprites = new ArrayList<>();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.sizeX = texture.getWidth() / frameWidth;
        this.sizeY = texture.getHeight() / frameHeight;
        this.heightOffset = heightOffset;
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                sprites.add(new Sprite(this, x, y, frameWidth, frameHeight));
            }
        }

        if (sprites.isEmpty()) {
            throw new LifeException("Empty sprite atlas " + texture.getName() + " | texWidth " + texture.getWidth() + " | texHeight " + texture.getHeight());
        }
    }

    @NotNull
    public static SpriteAtlas create(Texture texture, int frameWidth, int frameHeight, int heightOffset) {
        return new SpriteAtlas(texture, frameWidth, frameHeight, heightOffset);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public String getName() {
        return texture.getName();
    }

    public int getHeightOffset() {
        return heightOffset;
    }

    @NotNull
    public Sprite getTextureSprite(int x, int y) {
        final var index = x + y * sizeX;
        if (index >= sizeX * sizeY) throw new LifeException("Bad sprite index: " + index + " | x: " + x + " | y: " + y + " | spritesSize: " + sprites.size());
        return sprites.get(index);
    }

    @NotNull
    public Sprite getHeightSprite(int x, int y) {
        final var index = x + (y + getHeightOffset()) * sizeX;
        if (index >= sizeX * sizeY) throw new LifeException("Bad sprite index: " + index + " | x: " + x + " | y: " + y);
        return sprites.get(index);
    }

    @NotNull
    public List<Sprite> getSprites() {
        return sprites;
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getTextureWidth() {
        return texture.getWidth();
    }

    public int getTextureHeight() {
        return texture.getHeight() / 2;
    }
}
