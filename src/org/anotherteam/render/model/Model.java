package org.anotherteam.render.model;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public final class Model {

    private final int indicsesSize;
    private final int vId;
    private final int tId;
    private final int iId;

    public Model(float[] vertices, float[] uv, int[] indices) {
        indicsesSize = indices.length;

        vId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        tId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(uv), GL_STATIC_DRAW);

        iId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iId);
        val buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, vId);
        glVertexPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tId);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iId);
        glDrawElements(GL_TRIANGLES, indicsesSize, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    private FloatBuffer createBuffer (float[] array) {
        val buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }
}
