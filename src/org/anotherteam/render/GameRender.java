package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.player.Player;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.EffectFrame;
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

    private final Vector2i position;
    private int width, height;

    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;
    private final RenderBatch resizeBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final EffectFrame effectFrame;
    public final ResizeFrame resizeFrame;

    private final Shader raycastShader;

    private final Font debugFont;

    public GameRender(@NotNull GameScreen screen) {
        this.gameScreen = screen;
        this.position = new Vector2i(0, 0);
        width = GameScreen.window.getWidth();
        height = GameScreen.window.getHeight();

        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(AssetsData.DEFAULT_SHADER, screen.gameCamera);
        effectBatch = new RenderBatch(raycastShader, screen.gameCamera);

        resizeBatch = new RenderBatch(AssetsData.DEFAULT_SHADER, screen.gameCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        effectFrame = new EffectFrame(effectBatch);
        resizeFrame = new ResizeFrame(resizeBatch, GameScreen.window.getWidth(), GameScreen.window.getHeight());

        debugFont = new Font("font/font.ttf", 16);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void render(@NotNull RenderBatch windowBatch, @NotNull Level level) {
        // Start frames
        heightFrame.begin();
        drawHeightMap(level);
        heightFrame.end();

        textureFrame.begin();
        drawTextures(level);
        textureFrame.end();

        raycastShader.bind();
        raycastShader.setUniform("player_pos",
                new Vector2i(Player.player.getPosition().x, Player.player.getPosition().y + 15));
        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        effectFrame.texture.bind(0);

        effectFrame.begin();
        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        effectFrame.end();

        resizeFrame.begin();
        resizeBatch.draw(
                effectFrame.texture, 0, 0, false, true);
        resizeFrame.end();
        //Finish frames

        glViewport(0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight());
        windowBatch.begin();
        windowBatch.draw(
                resizeFrame.texture, position.x, position.y, width, height, false, true);
        if (Game.DebugMode)
            windowBatch.drawText(debugFont, "Pos : " + inGameMouseX() + " " + inGameMouseY(),
                    (int)(Input.getMousePos().x + 15), (int)(Input.getMousePos().y - 25), 1.0f, new Color(255, 255, 255, 255));
        windowBatch.end();
    }

    private int inGameMouseX() {
        if (Input.getMousePos().x < position.x || Input.getMousePos().x > position.x + width) return -1;
        return (int) (((Input.getMousePos().x - position.x) / width) * GameScreen.WIDTH);
    }

    private int inGameMouseY() {
        if (Input.getMousePos().x < position.x || Input.getMousePos().y > position.y + height) return -1;
        return (int) (((Input.getMousePos().y - position.y) / height) * GameScreen.HEIGHT);
    }

    private void drawTextures(@NotNull Level level) {
        for (val room : level.getRooms()) {
            room.drawTexture(textureBatch);
        }
    }

    private void drawHeightMap(@NotNull Level level) {
        for (val room : level.getRooms()) {
            room.drawHeight(textureBatch);
        }
    }
}