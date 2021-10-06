package org.anotherteam.render.texture;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class Texture {

    private final int id;
    private final int width;
    private final int height;

    private final ByteBuffer pixels;

    public Texture(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException error) {
            error.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();

        pixels = BufferUtils.createByteBuffer(width * height * 4);

        val pixels_raw = image.getRGB(0, 0, width, height,
                null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                val pixel = pixels_raw[x + y * width];
                pixels.put((byte)((pixel >> 16) & 0xFF)); //Red
                pixels.put((byte)((pixel >> 8) & 0xFF));  //Green
                pixels.put((byte)(pixel & 0xFF));         //Blue
                pixels.put((byte)((pixel >> 24) & 0xFF)); //Alpha
            }
        }
        pixels.flip();

        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }
}
