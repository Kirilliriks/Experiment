package org.anotherteam.screen;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class GameScreen {
    public static final Vector2i POSITION = new Vector2i(0, 0);
    public static final int RENDER_SCALE = 5;
    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    //EDITOR
    public static Sprite draggedSprite = null;

    public static RenderBatch windowBatch;

    public static Window window;

    public final Camera gameCamera;
    public final Camera windowCamera;

    public GameScreen(@NotNull Window window) {
        this.windowCamera = new Camera(0, 0, window.getWidth(), window.getHeight());
        this.gameCamera = new Camera(0, 0, WIDTH, HEIGHT);

        GameScreen.window = window;
        windowBatch = new RenderBatch(AssetData.DEFAULT_SHADER, windowCamera);
    }

    public static int inGameMouseX() {
        val renderWidth = GameScreen.WIDTH * RENDER_SCALE;
        if (Input.getMouseX() < POSITION.x || Input.getMouseX() > POSITION.x + renderWidth) return -1;
        return (int) (((Input.getMouseX() - POSITION.x) / renderWidth) * GameScreen.WIDTH);
    }

    public static int inGameMouseY() {
        val renderHeight = GameScreen.HEIGHT * RENDER_SCALE;
        if (Input.getMouseY() < POSITION.y || Input.getMouseY() > POSITION.y + renderHeight) return -1;
        return (int) (((Input.getMouseY() - POSITION.y) / renderHeight) * GameScreen.HEIGHT);
    }
}
