package org.anotherteam.screen;

import org.anotherteam.render.screen.Camera;
import org.anotherteam.render.window.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class GameScreen {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    public final Window window;

    public final Camera gameCamera;
    public final Camera windowCamera;
    public final Vector3f cursorPosition;

    public GameScreen(@NotNull Window window) {
        this.window = window;
        this.gameCamera = new Camera(0, 0, WIDTH, HEIGHT);
        this.windowCamera = new Camera(0, 0, window.getWidth(), window.getHeight());
        this.cursorPosition = new Vector3f(0, 0, 0);
    }
}
