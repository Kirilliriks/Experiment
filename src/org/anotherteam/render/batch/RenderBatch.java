package org.anotherteam.render.batch;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.text.Font;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.util.Color;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class RenderBatch extends Batch {

    private static final Vector2i[] QUAD_OFFSET = new Vector2i[] {
            new Vector2i(0, 1),
            new Vector2i(1, 1),
            new Vector2i(1, 0),
            new Vector2i(0, 0)
    };

    private static final short TEX_COORDS_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final short COLOR_COORDS_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private static final short VERTEX_SIZE = 7;

    protected Texture lastTexture;
    public boolean blend;

    public RenderBatch(@NotNull Shader shader, @NotNull Camera camera) {
        super(shader, camera, VERTEX_SIZE);
        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, vertexSizeBytes, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, COLOR_SIZE, GL_FLOAT, false, vertexSizeBytes, COLOR_COORDS_OFFSET);
        glEnableVertexAttribArray(2);
        blend = true;
        lastTexture = null;
    }

    @Override
    public void begin() {
        clear();
        shader.bind();
    }

    @Override
    public void end() {
        if (numQuads > 0)
            render();

        lastTexture = null;
        shader.unbind();
    }

    public void setBlend(boolean blend) {
        this.blend = blend;
    }

    private void changeTexture(Texture texture) {
        render();
        lastTexture = texture;
    }

    public void render() {
        if (numQuads == 0) return;

        if (blend) {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA, GL_ONE);
        } else {
            glDisable(GL_BLEND);
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.setUniform("projection", camera.getProjection());
        shader.setUniform("view", camera.getViewMatrix());
        lastTexture.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, numQuads * INDICES_PRE_QUAD, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
        if (blend) {
            glDisable(GL_BLEND);
        }

        numQuads = 0;
    }

    public void draw(Sprite sprite, float x, float y) {
        draw(sprite.getTexture(), x, y, sprite.getWidth(),
                sprite.getHeight(),
                false, false,
                Color.WHITE, sprite.getTextCoords());
    }

    public void draw(Sprite sprite, float x, float y, boolean flipX) {
        draw(sprite.getTexture(), x, y, sprite.getWidth(),
                sprite.getHeight(),
                flipX, false,
                Color.WHITE, sprite.getTextCoords());
    }

    public void draw(Texture texture, Vector2i position) {
        draw(texture, position.x, position.y);
    }

    public void draw(Texture texture, float x, float y) {
        draw(texture, x, y, false);
    }

    public void draw(Texture texture, float x, float y, boolean flipX) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), flipX, false);
    }

    public void draw(Texture texture, float x, float y, boolean flipX, boolean flipY) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), flipX, flipY);
    }

    public void draw(Texture texture,
                     float x, float y,
                     int width, int height,
                     boolean flipX, boolean flipY) {
        draw(texture, x, y, width, height, flipX, flipY, Color.WHITE);
    }

    public void draw(Texture texture,
                     float x, float y,
                     int width, int height,
                     boolean flipX, boolean flipY,
                     Color color) {
        val textureCoords = Texture.DEFAULT_COORDS;

        draw(texture, x, y, width, height, flipX, flipY, color, textureCoords);
    }

    public void draw(Texture texture,
                     float x, float y,
                     int width, int height,
                     boolean flipX, boolean flipY,
                     Color color,
                     Vector2f[] texCoords) {
        if (lastTexture != texture) {
            changeTexture(texture); // Before changeTexture calls - render()
        } else if (numQuads + 1 > batchSize) render();

        genQuad(x, y, width, height, texCoords, flipX, flipY, color, numQuads);
        numQuads++;
    }

    private void genQuad(float x, float y,
                         int width, int height,
                         Vector2f[] texCoords,
                         boolean flipX, boolean flipY,
                         Color color,
                         int index) {

        int offset = index * QUAD_POS_SIZE * VERTEX_SIZE;

        val x0 = flipX ? texCoords[1].x : texCoords[0].x;
        val x1 = flipX ? texCoords[0].x : texCoords[1].x;
        val y0 = flipY ? texCoords[2].y : texCoords[1].y;
        val y1 = flipY ? texCoords[1].y : texCoords[2].y;

        float r = color.r / 255.0f;
        float g = color.g / 255.0f;
        float b = color.b / 255.0f;

        // Load position
        vertices[offset] = x + QUAD_OFFSET[0].x * width;
        vertices[offset + 1] = y + QUAD_OFFSET[0].y  * height;

        // Load texture coords
        vertices[offset + 2] = x0;
        vertices[offset + 3] = y0;

        // Load color
        vertices[offset + 4] = r;
        vertices[offset + 5] = g;
        vertices[offset + 6] = b;
        offset += VERTEX_SIZE;

        vertices[offset] = x + QUAD_OFFSET[1].x * width;
        vertices[offset + 1] = y + QUAD_OFFSET[1].y  * height;
        vertices[offset + 2] = x1;
        vertices[offset + 3] = y0;
        vertices[offset + 4] = r;
        vertices[offset + 5] = g;
        vertices[offset + 6] = b;
        offset += VERTEX_SIZE;

        vertices[offset] = x + QUAD_OFFSET[2].x * width;
        vertices[offset + 1] = y + QUAD_OFFSET[2].y  * height;
        vertices[offset + 2] = x1;
        vertices[offset + 3] = y1;
        vertices[offset + 4] = r;
        vertices[offset + 5] = g;
        vertices[offset + 6] = b;
        offset += VERTEX_SIZE;

        vertices[offset] = x + QUAD_OFFSET[3].x * width;
        vertices[offset + 1] = y + QUAD_OFFSET[3].y  * height;
        vertices[offset + 2] = x0;
        vertices[offset + 3] = y1;
        vertices[offset + 4] = r;
        vertices[offset + 5] = g;
        vertices[offset + 6] = b;
    }

    public void drawText(Font font, String text, int x, int y, float scale, Color color) {
       drawText(font, text, x, y, scale, color, false);
    }

    public void drawText(Font font, String text, int x, int y, float scale, Color color, boolean invertWidth) {
        int offsetX = 0;
        if (invertWidth) offsetX -= font.getTextWidth(text, scale);
        for (int i = 0; i < text.length(); i++) {
            val charInfo = font.getCharacter(text.charAt(i));
            if (charInfo.width == 0)
                throw new LifeException("Unknown font character " + text.charAt(i));

            draw(font.texture, x + offsetX, y,
                    (int)(charInfo.width * scale), (int)(charInfo.height * scale),
                    false, false,
                    color,
                    charInfo.texCoords);
            x += charInfo.width * scale;
        }
    }
}
