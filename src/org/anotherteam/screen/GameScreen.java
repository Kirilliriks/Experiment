package org.anotherteam.screen;

import org.anotherteam.input.Input;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.frame.RenderFrame;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class GameScreen {

    public static final int DEFAULT_WIDTH = 160;
    public static final int DEFAULT_HEIGHT = 90;
    public static final Camera GAME_CAMERA = new Camera(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    public static final Camera WINDOW_CAMERA = new Camera();

    public static int RENDER_WIDTH = DEFAULT_WIDTH;
    public static int RENDER_HEIGHT = DEFAULT_HEIGHT;

    public static int WIDTH = DEFAULT_WIDTH;
    public static int HEIGHT = DEFAULT_HEIGHT;

    private static RenderFrame windowFrame = null;
    private static Window window = null;
    private static DraggedThing draggedThing = null;

    private GameScreen() {
        throw new LifeException("Try create GameScreen object");
    }

    public static void init(@NotNull Window window) {
        WINDOW_CAMERA.setPosition(window.getWidth() / 2.0f, window.getHeight() / 2.0f);
        WINDOW_CAMERA.setProjection(window.getWidth(), window.getHeight());

        final var windowBatch = new RenderBatch(AssetData.DEFAULT_SHADER, WINDOW_CAMERA);
        windowFrame = new RenderFrame(windowBatch, window.getWidth(), window.getHeight());

        RENDER_WIDTH = window.getWidth();
        RENDER_HEIGHT = window.getHeight();
    }

    /**
     * Method return in game position mouse X
     *
     * @return in game position mouse X
     */
    public static int inGameMouseX() {
        final var x = inGameWindowMouseX();
        if (x < 0) return -1;
        return (int) (GAME_CAMERA.getPositionX() + x - WIDTH / 2.0f);
    }

    /**
     * Method return in game position mouse Y
     *
     * @return in game position mouse Y
     */
    public static int inGameMouseY() {
        final var y = inGameWindowMouseY();
        if (y < 0) return -1;
        return (int) (GAME_CAMERA.getPositionY() + y - HEIGHT / 2.0f);
    }

    public static boolean isMouseOnGameWindow() {
        if (Input.getMouseX() < 0 || Input.getMouseX() > RENDER_WIDTH) return false;
        if (Input.getMouseY() < 0 || Input.getMouseY() > RENDER_HEIGHT) return false;
        return true;
    }

    /**
     * Method return in game window position mouse X
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     *
     * @return in game window position mouse X
     */
    public static int inGameWindowMouseX() {
        if (Input.getMouseX() < 0 || Input.getMouseX() > RENDER_WIDTH) return -1;
        return (int) (((Input.getMouseX() - 0) / RENDER_WIDTH) * WIDTH);
    }

    /**
     * Method return in game window position mouse Y
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     *
     * @return in game window position mouse Y
     */
    public static int inGameWindowMouseY() {
        if (Input.getMouseY() < 0 || Input.getMouseY() > RENDER_HEIGHT) return -1;
        return (int) (((Input.getMouseY()) / RENDER_HEIGHT) * HEIGHT);
    }

    public static int mouseOnTileX() {
        final var x = inGameMouseX();
        if (x < 0) return -1;
        return x / Tile.SIZE.x;
    }

    public static int mouseOnTileY() {
        final var y = inGameMouseY();
        if (y < 0) return -1;
        return y / Tile.SIZE.y;
    }

    public static float toWindowPosX(float x) {
        return (((x - GAME_CAMERA.getPositionX()) * RENDER_WIDTH) / WIDTH + RENDER_WIDTH / 2.0f);
    }

    public static float toWindowPosY(int y) {
        return (((y - GAME_CAMERA.getPositionY()) * RENDER_HEIGHT) / HEIGHT + RENDER_HEIGHT / 2.0f);
    }

    public static Vector2f toWindowPos(Vector2f vector) {
        vector.x = toWindowPosX((int) vector.x);
        vector.y = toWindowPosY((int) vector.y);
        return vector;
    }

    public static RenderFrame getWindowFrame() {
        return windowFrame;
    }

    public static void setWindow(Window window) {
        GameScreen.window = window;
    }

    public static Window getWindow() {
        return window;
    }

    public static void setDraggedThing(DraggedThing draggedThing) {
        GameScreen.draggedThing = draggedThing;
    }

    public static DraggedThing getDraggedThing() {
        return draggedThing;
    }
}
