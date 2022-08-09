package org.anotherteam.render.batch;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public abstract class Batch {

    public static final int DEFAULT_SIZE = 1000;

    public final DebugBatch debugBatch;

    // Vertex offset
    // ------
    // Pos              tex coords      color
    // float, float     float, float    float, float, float

    protected static final Vector2i[] QUAD_OFFSET = new Vector2i[] {
            new Vector2i(0, 1),
            new Vector2i(1, 1),
            new Vector2i(1, 0),
            new Vector2i(0, 0)
    };

    protected static final short QUAD_POS_SIZE = 4;
    protected static final short INDICES_PRE_QUAD = 6;

    protected static final short POS_SIZE = 2;        // x, y
    protected static final short TEX_COORDS_SIZE = 2; // u, v
    protected static final short COLOR_SIZE = 3;      // r, g, b

    protected static final short POS_OFFSET = 0;

    protected final short vertexSize;
    protected final short vertexSizeBytes;

    protected final int batchSize;
    protected final float[] vertices;

    protected final int vaoID, vboID, iboID;
    protected final Shader shader;

    @NotNull
    protected Camera camera;
    protected int numQuads;

    public Batch(@NotNull Shader shader, @NotNull Camera camera, short vertexSize) {
        debugBatch = new DebugBatch(camera);

        this.vertexSize = vertexSize;
        vertexSizeBytes = (short) (vertexSize * Float.BYTES);

        this.camera = camera;

        batchSize = DEFAULT_SIZE; // default
        vertices = new float[batchSize * QUAD_POS_SIZE * vertexSize];

        this.shader = shader;
        numQuads = 0;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        iboID = glGenBuffers();
        val indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, vertexSizeBytes, POS_OFFSET);
        glEnableVertexAttribArray(0);
    }

    public void setCamera(@NotNull Camera camera) {
        render();
        this.camera = camera;
    }

    public void begin() {
        begin(true);
    }

    public void begin(boolean clear) {
        if (clear) clear();
        shader.bind();
    }

    public void end() {
        if (numQuads > 0)
            render();
        shader.unbind();
    }

    public abstract void render();

    protected int[] generateIndices() {
        val elements = new int[INDICES_PRE_QUAD * batchSize];
        for (int i = 0; i < batchSize; i++) {
            generateIndexElement(elements, i);
        }
        return elements;
    }

    protected void generateIndexElement(int[] elements, int index) {
        val offsetArrayIndex = INDICES_PRE_QUAD * index;
        val offset = QUAD_POS_SIZE * index;

        elements[offsetArrayIndex] = offset;
        elements[offsetArrayIndex + 1] = offset + 1;
        elements[offsetArrayIndex + 2] = offset + 2;

        elements[offsetArrayIndex + 3] = offset + 2;
        elements[offsetArrayIndex + 4] = offset + 3;
        elements[offsetArrayIndex + 5] = offset;
    }

    public void clear() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
