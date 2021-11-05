package org.anotherteam.render;
import static org.lwjgl.opengl.GL42.*;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.GameState;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.screen.DraggedGameObject;
import org.anotherteam.editor.screen.DraggedTile;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.object.entity.Player;
import org.anotherteam.level.room.tile.Tile;
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

public final class GameRender {

    private final GameScreen gameScreen;

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

        raycastShader = new Shader("shader/vsInvert.glsl", "shader/fsInvert.glsl");

        textureBatch = new RenderBatch(AssetData.DEFAULT_SHADER, screen.gameCamera);
        effectBatch = new RenderBatch(raycastShader, screen.gameCamera);

        resizeBatch = new RenderBatch(AssetData.DEFAULT_SHADER, screen.gameCamera);

        textureFrame = new TextureFrame(textureBatch);
        heightFrame = new HeightFrame(textureBatch);
        effectFrame = new EffectFrame(effectBatch);
        resizeFrame = new ResizeFrame(resizeBatch, GameScreen.window.getWidth(), GameScreen.window.getHeight());

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
        raycastShader.setUniform("player_pos",
                room.getPlayer().getPosition().x, room.getPlayer().getPosition().y + 15);
        glBindImageTexture(1, heightFrame.texture.getId(), 0, false, 0, GL_READ_ONLY, GL_RGBA8);
        raycastShader.setUniform("u_texture", 0);
        effectFrame.texture.bind(0);

        effectFrame.begin();
        effectBatch.draw(
                textureFrame.texture, 0, 0, false, true);
        effectFrame.end();

        resizeFrame.begin();
        if (Game.game.getGameState() == GameState.ON_LEVEL) {
            resizeBatch.draw(
                    effectFrame.texture, 0, 0, false, true);
        } else if (Game.game.getGameState() == GameState.ON_EDITOR) {
            resizeBatch.draw(
                    textureFrame.texture, 0, 0, false, true);
        }
        resizeFrame.end();
        //Finish frames

        glViewport(0, 0, GameScreen.window.getWidth(), GameScreen.window.getHeight());
        windowBatch.begin();
        windowBatch.draw(
                resizeFrame.texture,
                GameScreen.POSITION.x, GameScreen.POSITION.y,
                GameScreen.WIDTH * GameScreen.RENDER_SCALE,
                GameScreen.HEIGHT * GameScreen.RENDER_SCALE,
                false, true);
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
            if (GameScreen.draggedThing instanceof DraggedGameObject draggedGameObject) {
                draggedGameObject.getGameObject().setPosition(mouseX, mouseY);
                draggedGameObject.getGameObject().render(textureBatch);
            } else if (GameScreen.draggedThing instanceof DraggedTile draggedTile){
                val x = (mouseX / Tile.SIZE.x) * Tile.SIZE.x;
                val y = (mouseY / Tile.SIZE.y) * Tile.SIZE.y;
                textureBatch.draw(draggedTile.getSprite(), x, y);
            } else textureBatch.draw(GameScreen.draggedThing.getSprite(), mouseX, mouseY);
        }
    }

    private void drawHeightMap(@NotNull Room room) {
        room.drawHeight(textureBatch);
    }
}