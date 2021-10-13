package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.EffectFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.ResizeFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class GameRender {
    private final GameScreen gameScreen;

    private final Camera renderCamera;
    private int width, height;

    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;

    /**
     * Render to player's window
     */
    private final RenderBatch renderBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final EffectFrame effectFrame;
    public final ResizeFrame resizeFrame;

    private final Shader defaultShader;
    private final Shader raycastShader;

    private final Font debugFont;

    private final Level level;

    public GameRender(@NotNull GameScreen screen, @NotNull Level level) {
        this.gameScreen = screen;
        this.level = level;
        width = 1920;
        height = 1080;
        renderCamera = new Camera(0, 0, width, height);

        defaultShader = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(defaultShader, screen.gameCamera);
        effectBatch = new RenderBatch(raycastShader, screen.gameCamera);

        renderBatch = new RenderBatch(defaultShader, renderCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        effectFrame = new EffectFrame(effectBatch);
        resizeFrame = new ResizeFrame(textureBatch, width, height);

        debugFont = new Font("font/font.ttf", 16);
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
        effectFrame.texture.bind(0);

        effectFrame.begin();
        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        effectFrame.end();

        resizeFrame.begin();
        textureBatch.draw(
                effectFrame.texture, 0, 0, false, true);
        resizeFrame.end();
        //Finish frames

        renderBatch.begin();
        renderBatch.draw(
                resizeFrame.texture, 0, 0, false, true);
        if (Game.DebugMode) {
            debugFont.drawText(renderBatch, "Pos; " + gameScreen.getMouseX() + " " + gameScreen.getMouseY(),
                    (int) Input.getMousePos().x, (int) Input.getMousePos().y - 40, 1.0f, new Color(255, 255, 255, 255));
        }
        renderBatch.end();
    }

    private void drawTextures() {
        for (val room : level.getRooms()) {
            room.drawTexture(textureBatch);
        }

        for (val gameObject : level.getGameObjects()) {
            gameObject.render(textureBatch);
        }
    }

    private void drawHeightMap() {
        for (val room : level.getRooms()) {
            room.drawHeight(textureBatch);
        }
    }

    @NotNull
    public GameScreen getGameScreen() {
        return gameScreen;
    }
}