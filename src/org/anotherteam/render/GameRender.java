package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.Room;
import org.anotherteam.object.GameObject;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.EffectFrame;
import org.anotherteam.render.frame.HeightFrame;
import org.anotherteam.render.frame.TextureFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class GameRender {

    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;

    public final TextureFrame textureFrame;
    public final HeightFrame heightFrame;
    public final EffectFrame effectFrame;

    private final Shader raycastShader;

    private final Camera renderCamera;

    public GameRender() {
        renderCamera = new Camera(GameScreen.WIDTH / 2, GameScreen.HEIGHT / 2, GameScreen.WIDTH, GameScreen.HEIGHT);

        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(AssetData.DEFAULT_SHADER, GameScreen.gameCamera);
        effectBatch = new RenderBatch(raycastShader, renderCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        effectFrame = new EffectFrame(effectBatch);
    }

    public void updateFrames() {
        textureFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);
        heightFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);
        effectFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);

        renderCamera.setProjection(GameScreen.WIDTH, GameScreen.HEIGHT);
        GameScreen.gameCamera.setProjection(GameScreen.WIDTH, GameScreen.HEIGHT);
        GameScreen.gameCamera.setPosition(GameScreen.WIDTH / 2.0f, GameScreen.HEIGHT / 2.0f);
    }

    public void render(@NotNull RenderBatch windowBatch, @NotNull Room room) {
        // Start frames
        heightFrame.begin();
        drawHeightMap(room);
        heightFrame.end();

        textureFrame.begin();
        drawTextures(room);
        textureFrame.end();

        effectFrame.begin();

        raycastShader.setUniform("real_view", GameScreen.gameCamera.getViewMatrix());

        val preparedX = GameScreen.gameCamera.translateX(room.getPlayer().getPosition().x);
        val preparedY = GameScreen.gameCamera.translateY(room.getPlayer().getPosition().y + 15);
        raycastShader.setUniform("player_pos",
                preparedX, preparedY);

        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        effectFrame.texture.bind(0);

        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);

        effectFrame.end();
        //Finish frames

        glViewport(0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight());
        windowBatch.begin();
        if (Game.stateManager.getState() == GameState.ON_LEVEL) {
            windowBatch.draw(
                    effectFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.RENDER_WIDTH,
                    GameScreen.RENDER_HEIGHT,
                    false, true);
        } else if (Game.stateManager.getState() == GameState.ON_EDITOR) {
            windowBatch.draw(
                    textureFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.RENDER_WIDTH,
                    GameScreen.RENDER_HEIGHT,
                    false, true);
            val v1 = new Vector2f(GameScreen.POSITION);
            val v2 = new Vector2f(GameScreen.POSITION.x + GameScreen.RENDER_WIDTH, GameScreen.POSITION.y);
            val v3 = new Vector2f(GameScreen.POSITION.x + GameScreen.RENDER_WIDTH, GameScreen.POSITION.y + GameScreen.RENDER_HEIGHT);
            val v4 = new Vector2f(GameScreen.POSITION.x, GameScreen.POSITION.y + GameScreen.RENDER_HEIGHT);
            windowBatch.debugBatch.drawLine(v1, v2, Color.RED);
            windowBatch.debugBatch.drawLine(v2, v3, Color.RED);
            windowBatch.debugBatch.drawLine(v3, v4, Color.RED);
            windowBatch.debugBatch.drawLine(v4, v1, Color.RED);
        }

        if (Game.DebugMode) {
            debugRender(windowBatch, room);
        }
        windowBatch.end();
    }

    private void drawTextures(@NotNull Room room) {
        room.draw(textureBatch, false);

        if (Game.stateManager.getState() != GameState.ON_EDITOR) return;

        if (GameScreen.draggedThing != null) {
            val mouseX = GameScreen.inGameMouseX();
            val mouseY = GameScreen.inGameMouseY();
            if (mouseX < 0 || mouseY < 0) return;

            GameScreen.draggedThing.render(mouseX, mouseY, textureBatch);
        }
    }

    private void drawHeightMap(@NotNull Room room) {
        room.draw(textureBatch, true);
    }

    private void debugRender(RenderBatch windowBatch, Room room) {
        int x = GameScreen.inGameMouseX();
        int y = GameScreen.inGameMouseY();
        windowBatch.drawText("Pos : " + x + " " + y,
                (int) (Input.getMousePos().x + 15), (int) (Input.getMousePos().y - 25));

        for (final GameObject object : room.getGameObjects()) {
            final Vector2i pos = object.getPosition();
            x = GameScreen.toWindowPosX(pos.x);
            y = GameScreen.toWindowPosY(pos.y);
            windowBatch.drawText("Pos: " + pos.x + " : " + pos.y, x, y);
        }
    }
}