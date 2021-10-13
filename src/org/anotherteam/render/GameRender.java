package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.FinalFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.ResizeFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class GameRender {
    private final GameScreen gameScreen;
    private final Level level;

    private final RenderBatch textureBatch;
    private final RenderBatch finalBatch;

    /**
     * Render to player's window
     */
    private final RenderBatch renderBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final FinalFrame finalFrame;
    public final ResizeFrame resizeFrame;

    private final Shader defaultShader;
    private final Shader raycastShader;

    private final Font font;

    public GameRender(@NotNull GameScreen screen, @NotNull Level level) {
        this.gameScreen = screen;
        this.level = level;
        defaultShader = new Shader("shader/defaultVertexShader.glsl", "shader/defaultFragmentShader.glsl");
        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(defaultShader, screen.gameCamera);
        finalBatch = new RenderBatch(raycastShader, screen.gameCamera);
        renderBatch = new RenderBatch(defaultShader, screen.gameCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        finalFrame = new FinalFrame(finalBatch);
        resizeFrame = new ResizeFrame(renderBatch, screen.window.getWidth(), screen.window.getHeight());

        font = new Font("font/Code 8x8.ttf", 16);
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
        finalFrame.texture.bind(0);

        finalFrame.begin();
        finalBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        finalFrame.end();

        resizeFrame.begin();
        renderBatch.setCamera(gameScreen.gameCamera);
        renderBatch.draw(
                finalFrame.texture, 0, 0, false, true);
        resizeFrame.end();
        //Finish frames

        renderBatch.begin();
        renderBatch.setCamera(gameScreen.windowCamera);
        renderBatch.draw(
                resizeFrame.texture, 0, 0, false, true);
        if (Game.DebugMode) {
            font.drawText(renderBatch, "Pos " + gameScreen.getMouseX() + " " + gameScreen.getMouseY(),
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