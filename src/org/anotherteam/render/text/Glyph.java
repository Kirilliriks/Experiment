package org.anotherteam.render.text;

import org.joml.Vector2f;

public class Glyph {
    public int sourceX;
    public int sourceY;
    public int width;
    public int height;

    public Vector2f[] texCoords = new Vector2f[4];

    public Glyph(int sourceX, int sourceY, int width, int height) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.width = width;
        this.height = height;
    }

    public void calculateTextureCoordinates(int fontWidth, int fontHeight) {
        float x0 = (float)sourceX / (float)fontWidth;
        float x1 = (float)(sourceX + width) / (float)fontWidth;
        float y0 = (float)(sourceY - height) / (float)fontHeight;
        float y1 = (float)(sourceY) / (float)fontHeight;

        texCoords[0] = new Vector2f(x0, y1);
        texCoords[1] = new Vector2f(x1, y0);
        texCoords[2] = new Vector2f(x1, y1);
        texCoords[3] = new Vector2f(x0, y0);
    }
}