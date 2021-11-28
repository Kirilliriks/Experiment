package org.anotherteam.screen;

import lombok.val;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class GameScreen {
    public static final Vector2i POSITION = new Vector2i(0, 0);
    public static final int RENDER_SCALE = 5;

    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    public static int RENDER_WIDTH = WIDTH * RENDER_SCALE;
    public static int RENDER_HEIGHT = HEIGHT * RENDER_SCALE;

    public static final Camera gameCamera = new Camera(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
    public static final Camera windowCamera = new Camera();

    public static RenderBatch windowBatch;
    public static Window window;

    public static DraggedThing draggedThing = null;

    private GameScreen() {
        throw new LifeException("Try create GameScreen object");
    }

    public static void init(@NotNull Window window) {
        windowCamera.setPosition(window.getWidth() / 2.0f, window.getHeight() / 2.0f);
        windowCamera.setProjection(window.getWidth(), window.getHeight());

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
        if (Input.getMouseX() < POSITION.x || Input.getMouseX() > POSITION.x + RENDER_WIDTH) return -1;
        return (int) (((Input.getMouseX() - POSITION.x) / RENDER_WIDTH) * GameScreen.WIDTH);
    }

    public static int inGameWindowMouseY() {
        if (Input.getMouseY() < POSITION.y || Input.getMouseY() > POSITION.y + RENDER_HEIGHT) return -1;
        return (int) (((Input.getMouseY() - POSITION.y) / RENDER_HEIGHT) * GameScreen.HEIGHT);
    }

    public static int onMouseTileX() {
        val x = inGameMouseX();
        if (x < 0) return -1;
        return x / Tile.SIZE.x;
    }

    public static int onMouseTileY() {
        val y = inGameMouseY();
        if (y < 0) return -1;
        return y / Tile.SIZE.y;
    }
}
