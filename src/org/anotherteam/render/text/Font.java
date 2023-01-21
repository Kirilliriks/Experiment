package org.anotherteam.render.text;

import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.FileUtils;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class Font {
    private final int fontSize;
    private final Map<Integer, Glyph> characterMap;

    private int width, height, lineHeight;
    private float scale;

    public Texture texture;

    public Font(String filepath, int fontSize) {
        this.fontSize = fontSize;
        this.scale = 1.0f;
        this.characterMap = new HashMap<>();
        generateBitmap(filepath);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @NotNull
    public Glyph getCharacter(int codepoint) {
        return characterMap.getOrDefault(codepoint, new Glyph(0, 0, 0, 0));
    }

    @Nullable
    private java.awt.Font registerFont(String filepath) {
        try {
            final File file = new File(filepath);
            if (!file.isFile()) {
                throw new LifeException("Font " + filepath + " not found");
            }

            final FileInputStream stream = new FileInputStream(file);

            final var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final var font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream);
            ge.registerFont(font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generateBitmap(String filepath) {
        java.awt.Font font = registerFont(filepath);
        if (font == null) {
            throw new LifeException("Bad font registering " + filepath);
        }

        font = new java.awt.Font(font.getName(), java.awt.Font.PLAIN, fontSize);

        var img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        var g2d = img.createGraphics();
        g2d.setFont(font);
        final var fontMetrics = g2d.getFontMetrics();

        final var estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;
        width = 0;
        height = fontMetrics.getHeight();
        lineHeight = fontMetrics.getHeight();
        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);

        for (int i = 0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) {
                final var charInfo = new Glyph(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                characterMap.put(i, charInfo);
                width = Math.max(x + fontMetrics.charWidth(i), width);

                x += charInfo.width;
                if (x <= estimatedWidth) continue;
                x = 0;
                y += fontMetrics.getHeight() * 1.4f;
                height += fontMetrics.getHeight() * 1.4f;
            }
        }
        height += fontMetrics.getHeight() * 1.4f;
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setFont(font);
        g2d.setColor(java.awt.Color.WHITE);
        for (int i = 0; i < font.getNumGlyphs(); i++) {
            if (!font.canDisplay(i)) continue;
            final var info = characterMap.get(i);
            info.calculateTextureCoordinates(width, height);
            g2d.drawString("" + (char)i, info.sourceX, info.sourceY);
        }
        g2d.dispose();

        uploadTexture(img);
    }

    private void uploadTexture(BufferedImage image) {
        final var pixels = new int[image.getHeight() * image.getWidth()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        final var buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final var alphaComponent = (byte) ((pixels[y * image.getWidth() + x] >> 24) & 0xFF);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
            }
        }
        buffer.flip();

        texture = new Texture(buffer, image.getWidth(), image.getHeight());
    }

    public int getTextWidth(String text, float scale) {
        int width = 0;

        for (int i = 0; i < text.length(); i++) {
            final var charInfo = getCharacter(text.charAt(i));
            if (charInfo.width == 0) {
                throw new LifeException("Unknown font character " + text.charAt(i));
            }

            width += charInfo.width * scale;
        }
        return width;
    }

    public int getTextHeight(String text, float scale) {
        final var charInfo = getCharacter(text.charAt(0));
        if (charInfo.width == 0) {
            throw new LifeException("Unknown font character " + text.charAt(0));
        }

        return (int) (charInfo.height * scale);
    }
}
