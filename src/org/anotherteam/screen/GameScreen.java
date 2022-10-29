package org.anotherteam.screen;

import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class GameScreen {
    public static final Vector2i POSITION = new Vector2i(0, 0);
    public static final int RENDER_SCALE = 5;
    public static int WIDTH = 160;
    public static int HEIGHT = 90;

    public static int RENDER_WIDTH = WIDTH * RENDER_SCALE;
    public static int RENDER_HEIGHT = HEIGHT * RENDER_SCALE;

    public static final Camera GAME_CAMERA = new Camera(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
    public static final Camera WINDOW_CAMERA = new Camera();

    public static RenderBatch windowBatch = null;
    public static Window window = null;
    public static DraggedThing draggedThing = null;

    private GameScreen() {
        throw new LifeException("Try create GameScreen object");
    }

    public static void init(@NotNull Window window) {
        WINDOW_CAMERA.setPosition(window.getWidth() / 2.0f, window.getHeight() / 2.0f);
        WINDOW_CAMERA.setProjection(window.getWidth(), window.getHeight());

        windowBatch = new RenderBatch(AssetData.DEFAULT_SHADER, WINDOW_CAMERA);
    }

    /**
     * Method return in game position mouse X
     * @return in game position mouse X
     */
    public static int inGameMouseX() {
        final var x = inGameWindowMouseX();
        if (x < 0) return -1;
        return (int) (GAME_CAMERA.getPositionX() + x - WIDTH / 2.0f);
    }

    /**
     * Method return in game position mouse Y
     * @return in game position mouse Y
     */
    public static int inGameMouseY() {
        final var y = inGameWindowMouseY();
        if (y < 0) return -1;
        return (int) (GAME_CAMERA.getPositionY() + y - HEIGHT / 2.0f);
    }

    /**
     * Method return in game window position mouse X
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     * @return in game window position mouse X
     */
    public static int inGameWindowMouseX() {
        if (Input.getMouseX() < POSITION.x || Input.getMouseX() > POSITION.x + RENDER_WIDTH) return -1;
        return (int) (((Input.getMouseX() - POSITION.x) / RENDER_WIDTH) * WIDTH);
    }

    /**
     * Method return in game window position mouse Y
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     * @return in game window position mouse Y
     */
    public static int inGameWindowMouseY() {
        if (Input.getMouseY() < POSITION.y || Input.getMouseY() > POSITION.y + RENDER_HEIGHT) return -1;
        return (int) (((Input.getMouseY() - POSITION.y) / RENDER_HEIGHT) * HEIGHT);
    }

    public static int onMouseTileX() {
        final var x = inGameMouseX();
        if (x < 0) return -1;
        return x / Tile.SIZE.x;
    }

    public static int onMouseTileY() {
        final var y = inGameMouseY();
        if (y < 0) return -1;
        return y / Tile.SIZE.y;
    }

    public static float toWindowPosX(float x) {
        return (POSITION.x + ((x - GAME_CAMERA.getPositionX()) * RENDER_WIDTH) / WIDTH + RENDER_WIDTH / 2f);
    }

    public static float toWindowPosY(int y) {
        return (POSITION.y + ((y - GAME_CAMERA.getPositionY()) * RENDER_HEIGHT) / HEIGHT + RENDER_HEIGHT / 2f);
    }

    public static Vector2f toWindowPos(Vector2f vector) {
        vector.x = toWindowPosX((int) vector.x);
        vector.y = toWindowPosY((int) vector.y);
        return vector;
    }
}
