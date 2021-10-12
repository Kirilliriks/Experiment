package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.FinalFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.LightFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class GameRender {
    private final GameScreen gameScreen;
    private final Level level;

    private final RenderBatch renderBatch;
    private final RenderBatch raycastBatch;

    private final RenderBatch textBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final LightFrame lightFrame;
    public final FinalFrame raycastFrame;

    private final Shader defaultShader;
    private final Shader raycastShader;

    private final Font font;

    public GameRender(@NotNull GameScreen screen, @NotNull Level level) {
        this.gameScreen = screen;
        this.level = level;
        defaultShader = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        renderBatch = new RenderBatch(defaultShader, screen.gameCamera);
        raycastBatch = new RenderBatch(raycastShader, screen.gameCamera);

        textBatch = new RenderBatch(defaultShader, screen.windowCamera);

        textureFrame = new TextureFrame(this, renderBatch);
        heightFrame = new HeightFrame(this, renderBatch);
        lightFrame = new LightFrame(this, raycastBatch);
        raycastFrame = new FinalFrame(this, raycastBatch);

        font = new Font("E:/VT323-Regular.ttf", 64);
    }

    public void render() {

        // Start frames
        heightFrame.begin();
        drawHeightMap();
        heightFrame.end();

        textureFrame.begin();
        drawTextures();
        textureFrame.end();

        raycastShader.bind();
        raycastShader.setUniform("player_pos",
                new Vector2i(EntityManager.player.getPosition().x, EntityManager.player.getPosition().y + 15));
        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        raycastFrame.texture.bind(0);

        raycastFrame.begin();
        raycastBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        raycastFrame.end();
        //Finish frames

        glViewport(0, 0, gameScreen.window.getWidth(), gameScreen.window.getHeight());
        renderBatch.begin();
        if (!Game.DebugMode) {

            renderBatch.draw(
                    raycastFrame.texture, 0, 0, false, true);
            renderBatch.end();
        } else {
            renderBatch.draw(
                    heightFrame.texture, 0, 0, false, true);
            renderBatch.end();
        }

        textBatch.begin();
        font.drawText(textBatch, "HELLO WORLD", 1, 400,1, 0xFF00AB0);
        textBatch.end();
    }

    private void drawTextures() {
        for (val room : level.getRooms()) {
            room.drawTexture(renderBatch);
        }

        for (val gameObject : level.getGameObjects()) {
            gameObject.render(renderBatch);
        }
    }

    private void drawHeightMap() {
        for (val room : level.getRooms()) {
            room.drawHeight(renderBatch);
        }
    }

    @NotNull
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    @NotNull
    public RenderBatch getRenderBatch() {
        return renderBatch;
    }
}