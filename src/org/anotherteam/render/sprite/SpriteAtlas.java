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

    public SpriteAtlas(String filePath, int frameWidth, int frameHeight) {
        this(new Texture(filePath), frameWidth, frameHeight);
    }

    public SpriteAtlas(Texture texture, int frameWidth, int frameHeight) {
        this.texture = texture;
        this.sprites = new ArrayList<>();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.sizeX = texture.getWidth() / frameWidth;
        this.sizeY = texture.getHeight() / frameHeight;
        for (int y = sizeY - 1; y >= 0; y--)
            for (int x = 0; x < sizeX; x++) {
                sprites.add(new Sprite(texture, x, y, frameWidth, frameHeight));
            }
    }

    @Nullable
    public Sprite getSprite(int x, int y) {
        if (x + y * sizeX >= sizeY * sizeX) return null;
        return sprites.get(x + y * sizeX);
    }
}
