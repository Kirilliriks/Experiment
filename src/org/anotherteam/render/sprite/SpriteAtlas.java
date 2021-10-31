package org.anotherteam.render.sprite;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class SpriteAtlas {

    private final Texture texture;
    private final List<Sprite> sprites;

    private final int frameWidth, frameHeight;
    private final int sizeX, sizeY;

    private final int heightOffset;

    public SpriteAtlas(String atlasName, int frameWidth, int frameHeight) {
        this(atlasName, frameWidth, frameHeight,  -1);
    }

    public SpriteAtlas(String atlasName, int frameWidth, int frameHeight, int heightOffset) {
        this(AssetData.getTexture(atlasName), frameWidth, frameHeight,  heightOffset);
    }

    private SpriteAtlas(Texture texture, int frameWidth, int frameHeight, int heightOffset) {
        this.texture = texture;
        this.sprites = new ArrayList<>();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.sizeX = texture.getWidth() / frameWidth;
        this.sizeY = texture.getHeight() / frameHeight;
        this.heightOffset = heightOffset;
        for (int y = 0; y < sizeY; y++)
            for (int x = 0; x < sizeX; x++) {
                sprites.add(new Sprite(this, x, y, frameWidth, frameHeight));
            }
    }

    @NotNull
    public static SpriteAtlas create(Texture texture, int frameWidth, int frameHeight, int heightOffset) {
        return new SpriteAtlas(texture, frameWidth, frameHeight, heightOffset);
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

    @Nullable
    public Sprite getSprite(int x, int y) {
        val index = x + (y * sizeX);
        if (index >= sizeY * sizeX) return null;
        return sprites.get(index);
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }
}
