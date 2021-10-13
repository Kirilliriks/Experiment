package org.anotherteam.screen;

import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class GameScreen {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    @NotNull
    public static RenderBatch windowBatch;
    @NotNull
    public static Window window;

    public final Camera gameCamera;
    public final Camera windowCamera;

    public GameScreen(@NotNull Window window) {
        this.windowCamera = new Camera(0, 0, window.getWidth(), window.getHeight());
        this.gameCamera = new Camera(0, 0, WIDTH, HEIGHT);

        GameScreen.window = window;
        windowBatch = new RenderBatch(AssetsData.DEFAULT_SHADER, windowCamera);
    }

    public static int inGameMouseX() {
        return (int) (Input.getMousePos().x / window.getWidth() * WIDTH);
    }

    public static int inGameMouseY() {
        return (int) (Input.getMousePos().y / window.getHeight() * HEIGHT);
    }
}
