package org.anotherteam.render.model;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public abstract class Model {

    protected final static short TEX_COORDS_NUM = 2;
    protected final static short AXIS_NUM = 2;

    protected float[] vertices;
    protected float[] uv;
    protected int[] indices;

    protected int vao;
    protected int pbo;
    protected int tbo;
    protected int ibo;

    public Model() { }

    public Model(float[] vertices, float[] uv, int[] indices) {
        this.vertices = vertices;
        this.uv = uv;
        this.indices = indices;
    }

    protected void generate() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        pbo = storeBuffer(createBuffer(vertices), 0, AXIS_NUM);

        tbo = storeBuffer(createBuffer(uv), 1, TEX_COORDS_NUM);

        ibo = glGenBuffers();
        val buffer = MemoryUtil.memAllocInt(indices.length);
        buffer.put(indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer.flip(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(pbo);
        glDeleteBuffers(tbo);
        glDeleteBuffers(ibo);
    }

    public void render() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private int storeBuffer(FloatBuffer buffer, int index, int size) {
        val bufferID = glGenBuffers();
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
