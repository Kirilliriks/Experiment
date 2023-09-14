package org.anotherteam.render.frame;

import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.texture.Pixmap;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public final class RenderFrame {

    public final RenderBatch renderBatch;

    public final FrameBuffer frameBuffer;
    public final Texture texture;
    public final Pixmap pixmap;

    public RenderFrame(@NotNull RenderBatch renderBatch) {
        this(renderBatch, GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    public RenderFrame(@NotNull RenderBatch renderBatch, int width, int height) {
        this.renderBatch = renderBatch;

        frameBuffer = new FrameBuffer(width, height);
        texture = frameBuffer.getTexture();
        pixmap = texture.getPixmap();
    }

    public void changeBufferSize(int width, int height) {
        frameBuffer.changeSize(width, height);
    }

    public void begin() {
        frameBuffer.begin();
        renderBatch.begin();
    }

    public void end() {
        renderBatch.end();
        frameBuffer.end();
    }

    public void fillBackground(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void renderBorders() {
        final var v1 = new Vector2f(0, 0);
        final var v2 = new Vector2f(GameScreen.RENDER_WIDTH, 0);
        final var v3 = new Vector2f(GameScreen.RENDER_WIDTH, GameScreen.RENDER_HEIGHT);
        final var v4 = new Vector2f(0, GameScreen.RENDER_HEIGHT);
        renderBatch.debugBatch.drawLine(v1, v2, Color.RED);
        renderBatch.debugBatch.drawLine(v2, v3, Color.RED);
        renderBatch.debugBatch.drawLine(v3, v4, Color.RED);
        renderBatch.debugBatch.drawLine(v4, v1, Color.RED);
    }
}
