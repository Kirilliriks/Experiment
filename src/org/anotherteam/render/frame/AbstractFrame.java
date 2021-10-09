package org.anotherteam.render.frame;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.render.GameRender;
import org.anotherteam.render.RenderBatch;
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

    public AbstractFrame(@NotNull GameRender gameRender) {
        this.gameRender = gameRender;
        gameScreen = gameRender.getGameScreen();
        renderBatch = gameRender.getRenderBatch();

        frameBuffer = new FrameBuffer(GameScreen.WIDTH, GameScreen.HEIGHT);
        texture = frameBuffer.getTexture();
        pixmap = texture.getPixmap();
        buffer = pixmap.getPixels();
    }

    public void begin() {
        frameBuffer.begin();
        renderBatch.begin();
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void end() {
        renderBatch.end();
        frameBuffer.end();
    }
}
