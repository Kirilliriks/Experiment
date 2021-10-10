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
    public final FinalFrame finalFrame;

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

        textureFrame = new TextureFrame(this);
        heightFrame = new HeightFrame(this);
        lightFrame = new LightFrame(this);
        finalFrame = new FinalFrame(this);
    }

    public void render() {
        heightFrame.begin();
        drawHeightLevel();
        heightFrame.end();

        textureFrame.begin();
        drawTextureLevel();
        textureFrame.end();

        raycastShader.bind();
        raycastShader.setUniform("player_pos",
                new Vector2i(EntityManager.player.getPosition().x, EntityManager.player.getPosition().y + 15));
        raycastShader.setUniform("u_texture1", 1);
        heightFrame.texture.bind(1);
        raycastShader.setUniform("u_texture", 0);
        finalFrame.texture.bind(0);

        finalFrame.begin();
        raycastBatch.begin();
        raycastBatch.draw(
                textureFrame.texture, 0, 0);
        raycastBatch.end();
        finalFrame.end();

        finalBatch.begin();
        if (!Game.DebugMode) {
            finalBatch.draw(
                    textureFrame.texture, 0, 0, false, true);
            finalBatch.end();
            return;
        } else {
            finalBatch.draw(
                    heightFrame.texture, 0, 0, false, true);
            finalBatch.end();
        }

        for (val object : level.getGameObjects()) {
            object.getCollider().debugRender(this);
        }
    }

    private void drawTextureLevel() {
        for (val room : level.getRooms()) {
            room.drawTexture(renderBatch);
        }

        for (val gameObject : level.getGameObjects()) {
            gameObject.drawTexture(renderBatch);
        }
    }

    private void drawHeightLevel() {
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