package org.anotherteam.debug;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class DebugBatch {
    public static DebugBatch GLOBAL = null;
    private static final short VERTEX_SIZE = 5;
    private static final short VERTEX_SIZE_BYTE = (short) (VERTEX_SIZE * Float.BYTES);
    private static final int MAX_LINES = 500;

    private static final short POS_OFFSET = 0;

    private static final short COLOR_OFFSET = (short) (2 * Float.BYTES);

    private static final Shader shader = AssetData.DEBUG_SHADER;

    private final Camera camera;

    private final int vaoID;
    private final int vboID;
    private final float[] vertices = new float[MAX_LINES * VERTEX_SIZE * 2];

    private int linesCount = 0;

    public DebugBatch(Camera camera) {
        this.camera = camera;
        // Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attributes
        glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEX_SIZE_BYTE, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, VERTEX_SIZE_BYTE, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void draw() {
        if (linesCount <= 0) return;

        // Bind the vao
        glBindVertexArray(vaoID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use our shader
        shader.bind();
        shader.setUniform("projection", camera.getProjection());
        shader.setUniform("view", camera.getViewMatrix());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the lines
        glLineWidth(2);
        glDrawArrays(GL_LINES, 0, linesCount * 2);
        glLineWidth(0);

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind shader
        shader.unbind();
        linesCount = 0;
    }

    public void drawLine(@NotNull Vector2f from, @NotNull Vector2f to, @NotNull Color color) {
        if (linesCount + 1 >= MAX_LINES) draw();

        int index = linesCount * VERTEX_SIZE * 2;
        for (int i = 0; i < 2; i++) {
            final Vector2f position = i == 0 ? from : to;

            // Load position
            vertices[index] = position.x;
            vertices[index + 1] = position.y;

            // Load the color
            vertices[index + 2] = color.r / 255.0f;
            vertices[index + 3] = color.g / 255.0f;
            vertices[index + 4] = color.b / 255.0f;

            index += VERTEX_SIZE;
        }
        linesCount++;
    }
}