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

    public static RenderBatch windowBatch;

    public final  Window window;

    public final Camera gameCamera;
    public final Camera windowCamera;

    public GameScreen(@NotNull Window window) {
        this.window = window;
        this.windowCamera = new Camera(0, 0, window.getWidth(), window.getHeight());
        this.gameCamera = new Camera(0, 0, WIDTH, HEIGHT);

        windowBatch = new RenderBatch(AssetsData.DEFAULT_SHADER, windowCamera);
    }

    public int getMouseX() {
        return (int) (Input.getMousePos().x / window.getWidth() * WIDTH);
    }

    public int getMouseY() {
        return (int) (Input.getMousePos().y / window.getHeight() * HEIGHT);
    }
}
