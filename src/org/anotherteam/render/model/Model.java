package org.anotherteam.render.model;
import static org.lwjgl.opengl.GL42.*;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

@Deprecated
public abstract class Model {

    protected final static short TEX_COORDS_SIZE = 2;
    protected final static short POS_SIZE = 2;

    protected float[] vertices;
    protected float[] uv;
    protected int[] indices;

    protected int vaoID;
    protected int pboID;
    protected int tboID;
    protected int iboID;

    protected void generate() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        pboID = storeBuffer(createBuffer(vertices), 0, POS_SIZE);

        tboID = storeBuffer(createBuffer(uv), 1, TEX_COORDS_SIZE);

        iboID = glGenBuffers();
        final var buffer = MemoryUtil.memAllocInt(indices.length);
        buffer.put(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer.flip(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glDeleteVertexArrays(vaoID);
        glDeleteBuffers(pboID);
        glDeleteBuffers(tboID);
        glDeleteBuffers(iboID);
    }

    public void render() {
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private int storeBuffer(FloatBuffer buffer, int index, int size) {
        final var bufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    private static FloatBuffer createBuffer(float[] array) {
        final var buffer = MemoryUtil.memAllocFloat(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }
}
