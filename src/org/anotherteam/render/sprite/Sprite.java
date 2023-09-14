package org.anotherteam.render.sprite;

import lombok.Getter;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Getter
public final class Sprite {

    public static final Vector2i DEFAULT_SIZE = new Vector2i(32, 32);

    private final Vector2f[] textCoords = new Vector2f[4];
    private final Texture texture;
    private final int width, height;
    private final int frameX, frameY;

    public Sprite(SpriteAtlas spriteAtlas, int frameX, int frameY, int width, int height) {
        this.texture = spriteAtlas.getTexture();
        this.frameX = frameX;
        this.frameY = frameY;
        this.width = width;
        this.height = height;

        for (short i = 0; i < 4; i++) {
            textCoords[i] = new Vector2f(0, 0);
        }

        calculateTextureCoords(spriteAtlas);
    }

    private void calculateTextureCoords(SpriteAtlas spriteAtlas) {
        final var correctFrame = spriteAtlas.getSizeY() - frameY - 1;
        final var x0 = (float) (width * frameX) / texture.getWidth();
        final var x1 = (float) (width * (frameX + 1)) / texture.getWidth();
        final var y0 = (float) (height * (correctFrame)) / texture.getHeight();
        final var y1 = (float) (height * (correctFrame + 1)) / texture.getHeight();
        textCoords[0].set(x0, y0);
        textCoords[1].set(x1, y0);
        textCoords[2].set(x1, y1);
        textCoords[3].set(x0, y1);
    }

    @NotNull
    public Vector2f[] getTextCoords() {
        return textCoords;
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }
    public float getU0() {
        return textCoords[0].x;
    }

    public float getV0() {
        return textCoords[0].y;
    }

    public float getU1() {
        return textCoords[2].x;
    }

    public float getV1() {
        return textCoords[2].y;
    }
}
