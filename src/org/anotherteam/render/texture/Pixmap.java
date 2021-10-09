package org.anotherteam.render.texture;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public final class Pixmap {

    private final ByteBuffer pixels;
    private final int width, height;

    public Pixmap(int width, int height) {
        this.pixels = BufferUtils.createByteBuffer(width * height * 4);
        this.width = width;
        this.height = height;
    }

    public Pixmap(@NotNull ByteBuffer buffer, int width, int height) {
        this.pixels = buffer;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawPixmap(@NotNull Pixmap drawPixmap, int x, int y) {
        val buffer = drawPixmap.getPixels();
        for (int yD = y; yD < height; yD++) {
            if (yD >= drawPixmap.height) break;
            for (int xD = x; xD < width; xD++) {
                if (xD >= drawPixmap.width) break;

                val drawIndex = (x + y * drawPixmap.width) * 4;
                val ownerIndex = (x + y * width) * 4;
                pixels.put(ownerIndex, buffer.get(drawIndex));
            }
        }
    }

    @NotNull
    public ByteBuffer getPixels() {
        return pixels;
    }
}
