package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.Room;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.EffectFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.render.text.Font;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class GameRender {

    private final GameScreen gameScreen;

    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final EffectFrame effectFrame;

    private final Shader raycastShader;

    private final Font debugFont;

    private final Camera renderCamera;

    public GameRender(@NotNull GameScreen screen) {
        this.gameScreen = screen;
        renderCamera = new Camera(GameScreen.WIDTH / 2, GameScreen.HEIGHT / 2, GameScreen.WIDTH, GameScreen.HEIGHT);

        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(AssetData.DEFAULT_SHADER, GameScreen.gameCamera);
        effectBatch = new RenderBatch(raycastShader, renderCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        effectFrame = new EffectFrame(effectBatch);

        debugFont = new Font("font/font.ttf", 16);
    }

    public void render(@NotNull RenderBatch windowBatch, @NotNull Room room) {
        // Start frames
        heightFrame.begin();
        drawHeightMap(room);
        heightFrame.end();

        textureFrame.begin();
        drawTextures(room);
        textureFrame.end();

        raycastShader.bind();
        raycastShader.setUniform("real_view", GameScreen.gameCamera.getViewMatrix());

        // TODO preparedPos need to correct translated object position's, in this example player's light position
        val preparedX = GameScreen.WIDTH / 2.0f + room.getPlayer().getPosition().x;
        val preparedY = GameScreen.HEIGHT / 2.0f + room.getPlayer().getPosition().y + 15;
        raycastShader.setUniform("player_pos",
                preparedX, preparedY);
        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        effectFrame.texture.bind(0);

        effectFrame.begin();
        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        effectFrame.end();
        //Finish frames

        glViewport(0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight());
        windowBatch.begin();
        if (Game.game.getGameState() == GameState.ON_LEVEL) {
            windowBatch.draw(
                    effectFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.WIDTH * GameScreen.RENDER_SCALE,
                    GameScreen.HEIGHT * GameScreen.RENDER_SCALE,
                    false, true);
        } else if (Game.game.getGameState() == GameState.ON_EDITOR) {
            windowBatch.draw(
                    textureFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.WIDTH * GameScreen.RENDER_SCALE,
                    GameScreen.HEIGHT * GameScreen.RENDER_SCALE,
                    false, true);
        }

        if (Game.DebugMode) {
            windowBatch.drawText(debugFont, "Pos : " + GameScreen.inGameMouseX() + " " + GameScreen.inGameMouseY(),
                    (int) (Input.getMousePos().x + 15), (int) (Input.getMousePos().y - 25), 1.0f, new Color(255, 255, 255, 255));
        }
        windowBatch.end();
    }

    private void drawTextures(@NotNull Room room) {
        room.drawTexture(textureBatch);

        if (Game.game.getGameState() == GameState.ON_LEVEL) return;

        if (GameScreen.draggedThing != null) {
            val mouseX = GameScreen.inGameMouseX();
            val mouseY = GameScreen.inGameMouseY();
            if (mouseX < 0 || mouseY < 0) return;

            GameScreen.draggedThing.render(mouseX, mouseY, textureBatch);
        }
    }

    private void drawHeightMap(@NotNull Room room) {
        room.drawHeight(textureBatch);
    }
}