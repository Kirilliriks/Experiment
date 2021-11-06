package org.anotherteam.screen;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class GameScreen {
    public static final Vector2i POSITION = new Vector2i(0, 0);
    public static final int RENDER_SCALE = 5;

    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    public final static Camera gameCamera = new Camera(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
    public final static Camera windowCamera = new Camera();

    public static RenderBatch windowBatch;
    public static Window window;

    public static DraggedThing draggedThing = null;

    public GameScreen(@NotNull Window window) {
        windowCamera.setPosition(window.getWidth() / 2.0f, window.getHeight() / 2.0f);
        windowCamera.setProjection(window.getWidth(), window.getHeight());

        GameScreen.window = window;
        windowBatch = new RenderBatch(AssetData.DEFAULT_SHADER, windowCamera);
    }

    public static int inGameMouseX() {
        val x = inGameWindowMouseX();
        if (x < 0) return -1;
        return (int) (gameCamera.getPosition().x + x - GameScreen.WIDTH / 2.0f);
    }

    public static int inGameMouseY() {
        val y = inGameWindowMouseY();
        if (y < 0) return -1;
        return (int) (gameCamera.getPosition().y + y - GameScreen.HEIGHT / 2.0f);
    }

    public static int inGameWindowMouseX() {
        val renderWidth = GameScreen.WIDTH * RENDER_SCALE;
        if (Input.getMouseX() < POSITION.x || Input.getMouseX() > POSITION.x + renderWidth) return -1;
        return (int) (((Input.getMouseX() - POSITION.x) / renderWidth) * GameScreen.WIDTH);
    }

    public static int inGameWindowMouseY() {
        val renderHeight = GameScreen.HEIGHT * RENDER_SCALE;
        if (Input.getMouseY() < POSITION.y || Input.getMouseY() > POSITION.y + renderHeight) return -1;
        return (int) (((Input.getMouseY() - POSITION.y) / renderHeight) * GameScreen.HEIGHT);
    }

    public static int onMouseTileX() {
        return inGameWindowMouseX() / Tile.SIZE.x;
    }

    public static int onMouseTileY() {
        return inGameWindowMouseY() / Tile.SIZE.y;
    }
}
