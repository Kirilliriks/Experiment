package org.anotherteam.util;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class PixmapUtil {

    public static ByteBuffer loadPixels(BufferedImage image) {
        final var width = image.getWidth();
        final var height = image.getHeight();
        final var buffer = BufferUtils.createByteBuffer(width * height * 4);
        final var pixels = image.getRGB(0, 0, width, height,
                null, 0, width);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                final var pixel = pixels[x + y * width];
                int r, g, b, a;
                r = ((pixel >> 16) & 0xFF);
                g = ((pixel >> 8) & 0xFF);
                b = ((pixel >> 0) & 0xFF);
                a = ((pixel >> 24) & 0xFF);
                buffer.put((byte)r); //Red
                buffer.put((byte)g);  //Green
                buffer.put((byte)b);         //Blue
                buffer.put((byte)a); //Alpha
            }
        return buffer.flip();
    }
}
