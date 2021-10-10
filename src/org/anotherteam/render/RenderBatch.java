package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.texture.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.w3c.dom.Text;

public final class RenderBatch {
    // Vertex offset
    // ------
    // Pos              tex coords
    // float, float     float, float

    private static final short QUAD_POS_SIZE = 4;
    private static final short INDICES_PRE_QUAD = 6;

    private static final short POS_SIZE = 2; // x, y
    private static final short TEX_COORDS_SIZE = 2; // u, v

    private static final short POS_OFFSET = 0;
    private static final short TEX_COORDS_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;

    private static final short VERTEX_SIZE = 4;
    private static final short VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final int batchSize;
    private final float[] vertices;

    private final int vaoID, vboID, iboID;
    private final Shader shader;

    private Camera camera;
    private Texture lastTexture;
    private int numQuads;

    public RenderBatch (Shader shader, Camera camera) {
        this.camera = camera;

        this.batchSize = 1000; // default
        vertices = new float[batchSize * QUAD_POS_SIZE * VERTEX_SIZE];
        lastTexture = null;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        iboID = glGenBuffers();
        val indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(1);

        this.shader = shader;
        numQuads = 0;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void begin() {
        clear();
        shader.bind();
    }

    public void end() {
        if (numQuads > 0)
            render();

        lastTexture = null;
        shader.unbind();
    }

    private void changeTexture(Texture texture) {
        render();
        lastTexture = texture;
    }

    public void render() {
        if (numQuads == 0) return;

        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA, GL_ONE);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.setUniform("projection", camera.getProjection());
        shader.setUniform("view", camera.getViewMatrix());
        lastTexture.bind();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numQuads * INDICES_PRE_QUAD, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
        glDisable(GL_BLEND);

        numQuads = 0;
    }

    public void clear() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void draw(Texture texture, Vector2i position) {
        draw(texture, position.x, position.y);
    }

    public void draw(Texture texture, Vector2i position, boolean flipX) {
        draw(texture, position.x, position.y, flipX);
    }

    public void draw(Texture texture, Vector2i position, boolean flipX, boolean flipY) {
        draw(texture, position.x, position.y, flipX, flipY);
    }

    public void draw(Texture texture, float x, float y) {
        draw(texture, x, y, false);
    }

    public void draw(Texture texture, float x, float y, boolean flipX) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(),
                0, 0, texture.getWidth(), texture.getHeight(), flipX, false);
    }

    public void draw(Texture texture, float x, float y, boolean flipX, boolean flipY) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(),
                0, 0, texture.getWidth(), texture.getHeight(), flipX, flipY);
    }

    public void draw(Texture texture,
                     float x, float y,
                     int width, int height,
                     float xTex, float yTex,
                     int texWidth, int texHeight,
                     boolean flipX, boolean flipY) {
        if (lastTexture != texture) {
            changeTexture(texture); // Before changeTexture calls - render()
        } else if (numQuads + 1 > batchSize) render();

        genQuad(texture, x, y, width, height, xTex, yTex,
                texWidth, texHeight,
                flipX, flipY, numQuads);
        numQuads++;
    }

    private void genQuad(Texture texture,
                         float x, float y,
                         int width, int height,
                         float xTex, float yTex,
                         int texWidth, int texHeight,
                         boolean flipX, boolean flipY,
                         int index) {
        val offsets = new Vector2i[] {
                new Vector2i(0, 1),
                new Vector2i(1, 1),
                new Vector2i(1, 0),
                new Vector2i(0, 0)
        };

        val textureCoords = Texture.DEFAULT_COORDS;
        val xU = flipX ? 1 : 0;
        val yU = flipY ? 1 : 0;
        val uv = new Vector2f[] {
                new Vector2f(((xTex + textureCoords[0].x + xU) * texWidth) / texture.getWidth(),
                             ((yTex + textureCoords[0].y + yU) * texHeight) / texture.getHeight()),

                new Vector2f(((xTex + textureCoords[1].x - xU) * texWidth) / texture.getWidth(),
                             ((yTex + textureCoords[1].y + yU) * texHeight) / texture.getHeight()),

                new Vector2f(((xTex + textureCoords[2].x - xU) * texWidth) / texture.getWidth(),
                             ((yTex + textureCoords[2].y - yU) * texHeight) / texture.getHeight()),

                new Vector2f(((xTex + textureCoords[3].x + xU) * texWidth) / texture.getWidth(),
                             ((yTex + textureCoords[3].y - yU) * texHeight) / texture.getHeight())
        };

        int offset = index * QUAD_POS_SIZE * VERTEX_SIZE;
        for (short i = 0; i < QUAD_POS_SIZE; i++) {

            // Load position
            vertices[offset] = x + offsets[i].x * width;
            vertices[offset + 1] = y + offsets[i].y  * height;

            // Load texture coords
            vertices[offset + 2] = uv[i].x;
            vertices[offset + 3] = uv[i].y;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        val elements = new int[INDICES_PRE_QUAD * batchSize];
        for (int i = 0; i < batchSize; i++) {
            generateIndexElement(elements, i);
        }
        return elements;
    }

    private void generateIndexElement(int[] elements, int index) {
        val offsetArrayIndex = INDICES_PRE_QUAD * index;
        val offset = QUAD_POS_SIZE * index;

        elements[offsetArrayIndex] = offset;
        elements[offsetArrayIndex + 1] = offset + 1;
        elements[offsetArrayIndex + 2] = offset + 2;

        elements[offsetArrayIndex + 3] = offset + 2;
        elements[offsetArrayIndex + 4] = offset + 3;
        elements[offsetArrayIndex + 5] = offset;
    }
}
