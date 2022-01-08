package org.anotherteam.debug;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class DebugRender {
    public static DebugRender global = null;
    private static final int VERTEX_SIZE = 5;
    private static final int MAX_LINES = 500;

    private static final Shader shader = AssetData.DEBUG_SHADER;

    private final Camera camera;

    private final int vaoID;
    private final int vboID;
    private final float[] vertices = new float[MAX_LINES * VERTEX_SIZE * 2];

    private int linesCount = 0;

    public DebugRender(Camera camera) {
        this.camera = camera;
        // Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_STATIC_DRAW);

        // Enable the vertex array attributes
        glVertexAttribPointer(0, 2, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public void draw() {
        if (linesCount <= 0) return;

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use our shader
        shader.bind();
        shader.setUniform("projection", camera.getProjection());
        shader.setUniform("view", camera.getViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, linesCount * 2);

        // Disable Location
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        // Unbind shader
        shader.unbind();
        linesCount = 0;
    }

    public void drawLine(@NotNull Vector2f from, @NotNull Vector2f to, @NotNull Color color) {
        if (linesCount + 1 >= MAX_LINES) draw();

        int index = linesCount * VERTEX_SIZE * 2;
        for (int i = 0; i < 2; i++) {
            val position = i == 0 ? from : to;

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