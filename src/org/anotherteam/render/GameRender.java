package org.anotherteam.render;

import org.anotherteam.game.Game;
import org.anotherteam.game.GameState;
import org.anotherteam.input.Input;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.shader.Shader;
import org.anotherteam.screen.Screen;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.opengl.GL42.*;

public final class GameRender {

    private final Game game;
    private final RenderBatch textureBatch;
    private final RenderBatch effectBatch;

    public final RenderFrame textureFrame;
    public final RenderFrame heightFrame;
    public final RenderFrame effectFrame;

    private final Shader raycastShader;

    private final Camera renderCamera;

    public GameRender(Game game) {
        this.game = game;

        renderCamera = new Camera(Screen.width / 2, Screen.height / 2, Screen.width, Screen.height);

        raycastShader = new Shader(AssetData.SHADER_PATH + "vsInvert.glsl", AssetData.SHADER_PATH + "fsInvert.glsl");

        textureBatch = new RenderBatch(AssetData.DEFAULT_SHADER, Screen.GAME_CAMERA);
        effectBatch = new RenderBatch(raycastShader, renderCamera);

        textureFrame = new RenderFrame(textureBatch);
        heightFrame = new RenderFrame(textureBatch);
        effectFrame = new RenderFrame(effectBatch);
    }

    public void updateFrames(int width, int height) {
        textureFrame.changeBufferSize(width, height);
        heightFrame.changeBufferSize(width, height);
        effectFrame.changeBufferSize(width, height);

        renderCamera.setProjection(width, height);
        Screen.GAME_CAMERA.setProjection(width, height);
    }

    public void render(@NotNull RenderFrame windowFrame, @NotNull Room room) {

        // Start frames
        heightFrame.begin();
        drawHeightMap(room);
        heightFrame.end();

        textureFrame.begin();
        textureFrame.fillBackground(0.0f, 0.0f, 0.0f, 1.0f);
        drawTextures(room);
        textureFrame.end();

        effectFrame.begin();

        raycastShader.setUniform("real_view", Screen.GAME_CAMERA.getViewMatrix());

        final int preparedX = Screen.GAME_CAMERA.translateX(room.getPlayer().getPosition().x);
        final int preparedY = Screen.GAME_CAMERA.translateY(room.getPlayer().getPosition().y + 15);
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

        final boolean onLevel = game.getState() == GameState.ON_LEVEL;
        windowFrame.renderBatch.draw(
                onLevel ? effectFrame.texture : textureFrame.texture,
                0, 0,
                Screen.getRenderWidth(),
                Screen.getRenderHeight(),
                false, true);

        if (Game.DEBUG) {
            debugRender(windowFrame.renderBatch, room);
        }

        windowFrame.end();

        windowFrame.renderBatch.begin();
        windowFrame.renderBatch.draw(
                windowFrame.texture,
                0, 0,
                Screen.getRenderWidth(),
                Screen.getRenderHeight(),
                false, true);
        windowFrame.renderBatch.end();
    }

    private void drawTextures(@NotNull Room room) {
        room.draw(textureBatch, false);

        if (!Game.DEBUG || Screen.getDraggedObject() == null) return;

        final int x = Screen.inGameMouseX();
        final int y = Screen.inGameMouseY();
        if (x < 0 || y < 0) return;

        Screen.getDraggedObject().draw(x, y, textureBatch);
    }

    private void drawHeightMap(@NotNull Room room) {
        room.draw(textureBatch, true);
    }

    private void debugRender(RenderBatch renderBatch, Room room) {
        final int x = Screen.inGameMouseX();
        final int y = Screen.inGameMouseY();
        renderBatch.drawText("Pos: " + x + " " + y,
                (int) (Input.getMousePos().x + 15), (int) (Input.getMousePos().y - 25));

        room.debugDraw(renderBatch);

        if (!Game.DEBUG || Screen.getDraggedObject() == null) return;
        if (x < 0 || y < 0) return;

        Screen.getDraggedObject().debugDraw(x, y, false, renderBatch);
    }
}