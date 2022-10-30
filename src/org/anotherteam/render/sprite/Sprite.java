package org.anotherteam.render.sprite;

import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Sprite {

    public static final Vector2i SIZE = new Vector2i(32, 32);

    private final SpriteAtlas spriteAtlas;
    private final Texture texture;
    private final Vector2f[] textCoords;
    private final int width, height;
    private final int frameX, frameY;

    public Sprite(SpriteAtlas spriteAtlas, int frameX, int frameY, int width, int height) {
        this.spriteAtlas = spriteAtlas;
        this.texture = spriteAtlas.getTexture();

        textCoords = new Vector2f[4];
        for (short i = 0; i < 4; i++) {
            textCoords[i] = new Vector2f(0, 0);
        }

        this.frameX = frameX;
        this.frameY = frameY;
        this.width = width;
        this.height = height;

        changeTextureCoords();
    }

    private void changeTextureCoords() {
        final var correctFrame = spriteAtlas.getSizeY() - frameY - 1;
        final var x0 = (float)(width * frameX) / texture.getWidth();
        final var x1 = (float)(width * (frameX + 1)) / texture.getWidth();
        final var y0 = (float)(height * (correctFrame)) / texture.getHeight();
        final var y1 = (float)(height * (correctFrame + 1)) / texture.getHeight();
        textCoords[0].set(x0, y0);
        textCoords[1].set(x1, y0);
        textCoords[2].set(x1, y1);
        textCoords[3].set(x0, y1);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    public Vector2f[] getTextCoords() {
        return textCoords;
    }

    @NotNull
    public Texture getTexture() {
        return texture;
    }

    @NotNull
    public SpriteAtlas getSpriteAtlas() {
        return spriteAtlas;
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
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
