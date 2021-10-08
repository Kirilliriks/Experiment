package org.anotherteam.render.model;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public final class Model {

    private final static short TEX_COORDS_NUM = 2;
    private final static short AXIS_NUM = 2;

    private final float[] vertices;
    private final float[] uv;
    private final int[] indices;

    private final int vao;
    private final int pbo;
    private final int ibo;

    public Model(float[] vertices, float[] uv, int[] indices) {
        this.vertices = vertices;
        this.uv = uv;
        this.indices = indices;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        pbo = storeBuffer(createBuffer(vertices), 0, AXIS_NUM);

        ibo = glGenBuffers();
        val buffer = MemoryUtil.memAllocInt(indices.length);
        buffer.put(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glVertexAttribPointer(1, TEX_COORDS_NUM, GL_FLOAT, false, 0, 0);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer.flip(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(pbo);
        glDeleteBuffers(ibo);
    }

    public void render() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        //glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        //glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private static int storeBuffer(FloatBuffer buffer, int index, int size) {
        int bufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    private static FloatBuffer createBuffer(float[] array) {
        val buffer = MemoryUtil.memAllocFloat(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }
}
