package org.anotherteam.util;

import lombok.val;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class PixmapUtil {

    public static ByteBuffer loadPixels(BufferedImage image) {
        val width = image.getWidth();
        val height = image.getHeight();
        val buffer = BufferUtils.createByteBuffer(width * height * 4);
        val pixels = image.getRGB(0, 0, width, height,
                null, 0, width);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                val pixel = pixels[x + y * width];
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
