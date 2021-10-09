package org.anotherteam.render.texture;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Texture {

    public static final Vector2i[] DEFAULT_COORDS = new Vector2i[] {
                new Vector2i(0, 0),
                new Vector2i(1, 0),
                new Vector2i(1, 1),
                new Vector2i(0, 1)
    };

    public static final Vector2i[] FRAMEBUFFER_COORDS = new Vector2i[] {
            new Vector2i(0, 1),
            new Vector2i(1, 1),
            new Vector2i(1, 0),
            new Vector2i(0, 0)
    };

    private final int id;
    private final int width;
    private final int height;

    private final Vector2i[] textureCoords;
    private final Pixmap pixmap;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        textureCoords = FRAMEBUFFER_COORDS;
        pixmap = new Pixmap(width, height);
        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public Texture(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException error) {
            error.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
        textureCoords = DEFAULT_COORDS;

        // Gen buffer
        val buffer = BufferUtils.createByteBuffer(width * height * 4);
        val pixels = image.getRGB(0, 0, width, height,
                null, 0, width);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                val pixel = pixels[x + y * width];
                buffer.put((byte)((pixel >> 16) & 0xFF)); //Red
                buffer.put((byte)((pixel >> 8) & 0xFF));  //Green
                buffer.put((byte)(pixel & 0xFF));         //Blue
                buffer.put((byte)((pixel >> 24) & 0xFF)); //Alpha
            }
        pixmap = new Pixmap(buffer.flip(), width, height);
        //

        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixmap.getPixels());
    }

    @NotNull
    public Vector2i[] getTextureCoords() {
        return textureCoords;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    public Pixmap getPixmap() {
        return pixmap;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void drawPixmap(@NotNull Pixmap drawPixmap, int x, int y) {
        bind();
        glTexSubImage2D(id, 0, x, y, drawPixmap.getWidth(), drawPixmap.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE,
                drawPixmap.getPixels());
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy() {
        glDeleteTextures(id);
    }
}
