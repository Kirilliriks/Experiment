package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.Room;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class GameRender {

    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;

    public final RenderFrame textureFrame;
    public final RenderFrame heightFrame;
    public final RenderFrame effectFrame;

    private final Shader raycastShader;

    private final Camera renderCamera;

    public GameRender() {
        renderCamera = new Camera(GameScreen.WIDTH / 2, GameScreen.HEIGHT / 2, GameScreen.WIDTH, GameScreen.HEIGHT);

        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(AssetData.DEFAULT_SHADER, GameScreen.GAME_CAMERA);
        effectBatch = new RenderBatch(raycastShader, renderCamera);

        textureFrame = new RenderFrame(textureBatch);
        heightFrame = new RenderFrame(textureBatch);
        effectFrame = new RenderFrame(effectBatch);
    }

    public void updateFrames() {
        textureFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);
        heightFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);
        effectFrame.changeBufferSize(GameScreen.WIDTH, GameScreen.HEIGHT);

        renderCamera.setProjection(GameScreen.WIDTH, GameScreen.HEIGHT);
        GameScreen.GAME_CAMERA.setProjection(GameScreen.WIDTH, GameScreen.HEIGHT);
        GameScreen.GAME_CAMERA.setPosition(GameScreen.WIDTH / 2.0f, GameScreen.HEIGHT / 2.0f);
    }

    public void render(@NotNull RenderFrame windowFrame, @NotNull Room room) {
        // Start frames
        heightFrame.begin();
        drawHeightMap(room);
        heightFrame.end();

        textureFrame.begin();
        drawTextures(room);
        textureFrame.end();

        effectFrame.begin();

        raycastShader.setUniform("real_view", GameScreen.GAME_CAMERA.getViewMatrix());

        final var preparedX = GameScreen.GAME_CAMERA.translateX(room.getPlayer().getPosition().x);
        final var preparedY = GameScreen.GAME_CAMERA.translateY(room.getPlayer().getPosition().y + 15);
        raycastShader.setUniform("player_pos",
                preparedX, preparedY);

        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        effectFrame.texture.bind(0);

        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);

        effectFrame.end();
        //Finish frames

        windowFrame.begin();
        if (Game.STATE_MANAGER.getState() == GameState.ON_LEVEL) {
            windowFrame.renderBatch.draw(
                    effectFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.RENDER_WIDTH,
                    GameScreen.RENDER_HEIGHT,
                    false, true);
        } else if (Game.STATE_MANAGER.getState() == GameState.ON_EDITOR) {
            windowFrame.renderBatch.draw(
                    textureFrame.texture,
                    GameScreen.POSITION.x, GameScreen.POSITION.y,
                    GameScreen.RENDER_WIDTH,
                    GameScreen.RENDER_HEIGHT,
                    false, true);
            final var v1 = new Vector2f(GameScreen.POSITION);
            final var v2 = new Vector2f(GameScreen.POSITION.x + GameScreen.RENDER_WIDTH, GameScreen.POSITION.y);
            final var v3 = new Vector2f(GameScreen.POSITION.x + GameScreen.RENDER_WIDTH, GameScreen.POSITION.y + GameScreen.RENDER_HEIGHT);
            final var v4 = new Vector2f(GameScreen.POSITION.x, GameScreen.POSITION.y + GameScreen.RENDER_HEIGHT);
            windowFrame.renderBatch.debugBatch.drawLine(v1, v2, Color.RED);
            windowFrame.renderBatch.debugBatch.drawLine(v2, v3, Color.RED);
            windowFrame.renderBatch.debugBatch.drawLine(v3, v4, Color.RED);
            windowFrame.renderBatch.debugBatch.drawLine(v4, v1, Color.RED);
        }

        if (Game.DEBUG_MODE) {
            debugRender(windowFrame.renderBatch, room);
        }

        windowFrame.end();

        glViewport(0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight());
        windowFrame.renderBatch.begin();
        windowFrame.renderBatch.draw(
                windowFrame.texture,
                GameScreen.POSITION.x, GameScreen.POSITION.y,
                GameScreen.RENDER_WIDTH,
                GameScreen.RENDER_HEIGHT,
                false, true);
        windowFrame.renderBatch.end();
    }

    private void drawTextures(@NotNull Room room) {
        room.draw(textureBatch, false);

        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;
        if (GameScreen.draggedThing == null) return;

        final int x = GameScreen.inGameMouseX();
        final int y = GameScreen.inGameMouseY();
        if (x < 0 || y < 0) return;

        GameScreen.draggedThing.draw(x, y, textureBatch);
    }

    private void drawHeightMap(@NotNull Room room) {
        room.draw(textureBatch, true);
    }

    private void debugRender(RenderBatch renderBatch, Room room) {
        final int x = GameScreen.inGameMouseX();
        final int y = GameScreen.inGameMouseY();
        renderBatch.drawText("Pos: " + x + " " + y,
                (int) (Input.getMousePos().x + 15), (int) (Input.getMousePos().y - 25));

        room.debugDraw(renderBatch);

        if (Game.STATE_MANAGER.getState() != GameState.ON_EDITOR) return;
        if (GameScreen.draggedThing == null) return;
        if (x < 0 || y < 0) return;

        GameScreen.draggedThing.debugDraw(x, y, false, renderBatch);
    }
}