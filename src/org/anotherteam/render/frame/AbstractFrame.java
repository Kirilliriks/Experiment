package org.anotherteam.render.frame;

import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.texture.Pixmap;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFrame {

    protected final RenderBatch renderBatch;

    public final FrameBuffer frameBuffer;
    public final Texture texture;
    public final Pixmap pixmap;

    public AbstractFrame(@NotNull RenderBatch renderBatch) {
        this(renderBatch, GameScreen.WIDTH, GameScreen.HEIGHT);
    }

    public AbstractFrame(@NotNull RenderBatch renderBatch, int width, int height) {
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
}
