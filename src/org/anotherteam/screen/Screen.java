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

public final class Screen {

    public static final int DEFAULT_WIDTH = 160;
    public static final int DEFAULT_HEIGHT = 90;
    public static final Camera camera = new Camera(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    public static final Camera windowCamera = new Camera();

    public static int width = DEFAULT_WIDTH;
    public static int height = DEFAULT_HEIGHT;

    private static RenderFrame mainFrame;
    private static Window window;
    private static DraggedObject draggedObject;

    private Screen() {
        throw new LifeException("Try create GameScreen object");
    }

    public static void init(@NotNull Window window) {
        Screen.window = window;
        mainFrame = new RenderFrame(new RenderBatch(AssetData.DEFAULT_SHADER, windowCamera), width, height);

        resize();
    }

    public static void resize() {
        final int width = getRenderWidth();
        final int height = getRenderHeight();
        windowCamera.setPosition(width/ 2.0f, height / 2.0f);
        windowCamera.setProjection(width, height);

        mainFrame.changeBufferSize(width, height);
    }

    /**
     * Method return in game position mouse X
     *
     * @return in game position mouse X
     */
    public static int inGameMouseX() {
        final var x = inGameWindowMouseX();
        if (x < 0) return -1;
        return (int) (camera.getPositionX() + x - width / 2.0f);
    }

    /**
     * Method return in game position mouse Y
     *
     * @return in game position mouse Y
     */
    public static int inGameMouseY() {
        final var y = inGameWindowMouseY();
        if (y < 0) return -1;
        return (int) (camera.getPositionY() + y - height / 2.0f);
    }

    public static boolean isMouseOnGameWindow() {
        if (Input.getMouseX() < 0 || Input.getMouseX() > window.getWidth()) return false;
        if (Input.getMouseY() < 0 || Input.getMouseY() > window.getHeight()) return false;
        return true;
    }

    /**
     * Method return in game window position mouse X
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     *
     * @return in game window position mouse X
     */
    public static int inGameWindowMouseX() {
        if (Input.getMouseX() < 0 || Input.getMouseX() > window.getWidth()) return -1;
        return (int) (((Input.getMouseX() - 0) / window.getWidth()) * width);
    }

    /**
     * Method return in game window position mouse Y
     * (That is, it returns the cursor in game coordinates without taking into account the camera offset)
     *
     * @return in game window position mouse Y
     */
    public static int inGameWindowMouseY() {
        if (Input.getMouseY() < 0 || Input.getMouseY() > window.getHeight()) return -1;
        return (int) (((Input.getMouseY()) / window.getHeight()) * height);
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
        return (((x - camera.getPositionX()) * window.getWidth()) / width + window.getWidth() / 2.0f);
    }

    public static float toWindowPosY(int y) {
        return (((y - camera.getPositionY()) * window.getHeight()) / height + window.getHeight() / 2.0f);
    }

    public static Vector2f toWindowPos(Vector2f vector) {
        vector.x = toWindowPosX((int) vector.x);
        vector.y = toWindowPosY((int) vector.y);
        return vector;
    }

    public static int getRenderWidth() {
        return window.getWidth();
    }

    public static int getRenderHeight() {
        return window.getHeight();
    }

    public static RenderFrame getMainFrame() {
        return mainFrame;
    }

    public static Window getWindow() {
        return window;
    }

    public static void setDraggedObject(DraggedObject draggedObject) {
        Screen.draggedObject = draggedObject;
    }

    public static DraggedObject getDraggedObject() {
        return draggedObject;
    }
}
