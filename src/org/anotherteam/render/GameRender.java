package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.frame.FinalFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.LightFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class GameRender {
    private final GameScreen gameScreen;
    private final Level level;

    private final RenderBatch renderBatch;
    private final RenderBatch raycastBatch;

    private final RenderBatch finalBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final LightFrame lightFrame;
    public final FinalFrame raycastFrame;

    private final Shader defaultShader;
    private final Shader raycastShader;

    public GameRender(@NotNull GameScreen screen, @NotNull Level level) {
        this.gameScreen = screen;
        this.level = level;
        defaultShader = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        renderBatch = new RenderBatch(defaultShader, screen.gameCamera);
        raycastBatch = new RenderBatch(raycastShader, screen.gameCamera);
        finalBatch = new RenderBatch(defaultShader, screen.renderCamera);

        textureFrame = new TextureFrame(this, renderBatch);
        heightFrame = new HeightFrame(this, renderBatch);
        lightFrame = new LightFrame(this, raycastBatch);
        raycastFrame = new FinalFrame(this, raycastBatch);
    }

    public void render() {
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

        finalBatch.begin();
        if (!Game.DebugMode) {
            finalBatch.draw(
                    raycastFrame.texture, 0, 0, false, true);
            finalBatch.end();
            return;
        } else {
            finalBatch.draw(
                    heightFrame.texture, 0, 0, false, true);
            finalBatch.end();
        }
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