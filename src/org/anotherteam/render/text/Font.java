package org.anotherteam.render.text;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import lombok.val;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.texture.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static java.awt.Font.TRUETYPE_FONT;

public class Font {

    private final Map<Character, Glyph> glyphs;
    private final Texture texture;
    private int fontHeight;

    public Font() {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), true);
    }
    public Font(boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), antiAlias);
    }

    public Font(int size) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), true);
    }

    public Font(int size, boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    public Font(InputStream in, int size) throws FontFormatException, IOException {
        this(in, size, true);
    }

    public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
        this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
    }

    public Font(java.awt.Font font) {
        this(font, true);
    }

    public Font(java.awt.Font font, boolean antiAlias) {
        glyphs = new HashMap<>();
        texture = createFontTexture(font, antiAlias);
    }

    @NotNull
    private Texture createFontTexture(java.awt.Font font, boolean antiAlias) {
        int imageWidth = 0;
        int imageHeight = 0;

        for (short i = 32; i < 256; i++) {
            if (i == 127) continue;

            BufferedImage ch = createCharImage(font, (char) i, antiAlias);
            if (ch == null) continue;

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        fontHeight = imageHeight;

        var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        val g = image.createGraphics();

        int x = 0;
        for (int i = 32; i < 256; i++) {
            if (i == 127) continue;

            val c = (char) i;

            BufferedImage charImage = createCharImage(font, c, antiAlias);
            if (charImage == null) continue;

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            val ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
            g.drawImage(charImage, x, 0, null);
            x += ch.width;
            glyphs.put(c, ch);
        }

        val transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        val operation = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        val width = image.getWidth();
        val height = image.getHeight();

        val pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        val buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {
                val pixel = pixels[i + j * width];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        buffer.flip();

        return new Texture(buffer, width, height);
    }

    @Nullable
    private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
        var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        var g = image.createGraphics();
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        val metrics = g.getFontMetrics();
        g.dispose();

        val charWidth = metrics.charWidth(c);
        val charHeight = metrics.getHeight();

        if (charWidth == 0) return null;

        /* Create image for holding the char */
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    public int getWidth(CharSequence text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            val c = text.charAt(i);
            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') continue;

            lineWidth += glyphs.get(c).width;
        }
        width = Math.max(width, lineWidth);
        return width;
    }

    public int getHeight(CharSequence text) {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i < text.length(); i++) {
            val c = text.charAt(i);
            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r') continue;
            lineHeight = Math.max(lineHeight, glyphs.get(c).height);
        }
        height += lineHeight;
        return height;
    }

    public void drawText(RenderBatch renderBatch, CharSequence text, float x, float y, Color c) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        texture.bind();
        renderBatch.begin();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= fontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = glyphs.get(ch);
            //renderer.drawTextureRegion(texture, drawX, drawY, g.x, g.y, g.width, g.height, c);
            drawX += g.width;
        }
        renderBatch.end();
    }

//    public void drawText(Renderer renderer, CharSequence text, float x, float y) {
//        //drawText(renderer, text, x, y, Color.WHITE);
//    }

    public void destroy() {
        texture.destroy();
    }

}
