package org.anotherteam.screen;

import org.anotherteam.render.screen.Camera;
import org.joml.Vector3f;

public class GameScreen {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 90;

    public final Camera camera;
    public final Vector3f cursorPosition;

    public GameScreen(int windowWidth, int windowHeight) {
        this.camera = new Camera(0, 0, windowWidth, windowHeight);
        this.cursorPosition = new Vector3f(0, 0, 0);
    }

    public Camera getCamera() {
        return camera;
    }
}
