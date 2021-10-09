package org.anotherteam.render.texture;

import lombok.val;
import org.anotherteam.util.PixmapUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class Pixmap {

    private final ByteBuffer pixels;
    private final int width, height;

    public Pixmap(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException error) {
            error.printStackTrace();
        }
        this.width = image.getWidth();
        this.height = image.getHeight();

        val buffer = BufferUtils.createByteBuffer(width * height * 4);
        val pixels = image.getRGB(0, 0, width, height,
                null, 0, width);
        PixmapUtil.fillPixels(buffer, pixels, width, height);

        this.pixels = BufferUtils.createByteBuffer(width * height * 4);
    }

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

    public void drawPixmap(@NotNull Pixmap pix, int xOwner, int yOwner) {
        drawPixmap(pix, xOwner, yOwner, 0, 0, pix.getWidth(), pix.getHeight());
    }

    public void drawPixmap(@NotNull Pixmap pix, int xOwner, int yOwner, int xPix, int yPix, int pixWidth, int pixHeight) {
        val buffer = pix.getPixels();
        for (int yD = yOwner; yD < height; yD++, xPix++) {
            if (yD >= pix.height || xPix >= pixWidth) break;
            for (int xD = xOwner; xD < width; xD++, yPix++) {
                if (xD >= pix.width || yPix >= pixHeight) break;

                val drawIndex = (xPix + yPix * pix.width) * 4;
                val ownerIndex = (xOwner + yOwner * width) * 4;
                pixels.put(ownerIndex, buffer.get(drawIndex));
            }
        }
    }

    @NotNull
    public ByteBuffer getPixels() {
        return pixels;
    }
}
