package org.anotherteam.render.sprite;

import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class SpriteAtlas {

    private final Texture texture;
    private final List<Sprite> sprites;

    private final int frameWidth, frameHeight;
    private final int sizeX, sizeY;

    public SpriteAtlas(Texture texture, int frameWidth, int frameHeight, int sizeX, int sizeY) {
        this.texture = texture;
        this.sprites = new ArrayList<>();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        for (int y = 0; y < sizeY; y++)
            for (int x = 0; x < sizeX; x++) {
                sprites.add(new Sprite(texture, x, y, frameWidth, frameHeight));
            }
    }

    @Nullable
    public Sprite getSprite(int x, int y) {
        if (x + y * sizeY >= sizeY * sizeX) return null;
        return sprites.get(x + y * sizeY);
    }
}
