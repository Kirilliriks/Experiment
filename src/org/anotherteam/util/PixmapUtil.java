package org.anotherteam.util;

import lombok.val;

import java.nio.ByteBuffer;

public final class PixmapUtil {

    public static ByteBuffer fillPixels(ByteBuffer buffer, int[] pixels, int width, int height) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                val pixel = pixels[x + y * width];
                buffer.put((byte)((pixel >> 16) & 0xFF)); //Red
                buffer.put((byte)((pixel >> 8) & 0xFF));  //Green
                buffer.put((byte)(pixel & 0xFF));         //Blue
                buffer.put((byte)((pixel >> 24) & 0xFF)); //Alpha
            }
        return buffer;
    }
}
