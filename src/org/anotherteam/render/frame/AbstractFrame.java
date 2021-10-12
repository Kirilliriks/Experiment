package org.anotherteam.render.frame;

import org.anotherteam.render.GameRender;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.framebuffer.FrameBuffer;
import org.anotherteam.render.texture.Pixmap;
import org.anotherteam.render.texture.Texture;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class AbstractFrame {

    protected final GameRender gameRender;
    protected final GameScreen gameScreen;
    protected final RenderBatch renderBatch;

    public final FrameBuffer frameBuffer;
    public final Texture texture;
    public final Pixmap pixmap;
    public final ByteBuffer buffer;

    public AbstractFrame(@NotNull GameRender gameRender, @NotNull RenderBatch renderBatch) {
        this.gameRender = gameRender;
        this.gameScreen = gameRender.getGameScreen();
        this.renderBatch = renderBatch;

        frameBuffer = new FrameBuffer(GameScreen.WIDTH, GameScreen.HEIGHT);
        texture = frameBuffer.getTexture();
        pixmap = texture.getPixmap();
        buffer = pixmap.getPixels();
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
