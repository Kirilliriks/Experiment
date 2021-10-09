package org.anotherteam;

import org.anotherteam.game.Game;
import org.anotherteam.game.Input;
import org.anotherteam.render.screen.Screen;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;

public final class Experimental implements Runnable {

    private Thread thread;
    private Game game;
    private Window window;

    public void start() {
        thread = new Thread(this, "thread");
        thread.start();
    }

    @Override
    public void run() {
        init();
        GL.createCapabilities(); // CRITICAL

        game = new Game(window);

        double beginTime = Time.getTime();
        double endTime;
        float dt = 0;
        float timeCount = 0;
        int frames = 0;
        int updates = 0;
        while (!window.shouldClose()) {
            window.update();

            game.update(dt);
            updates++;

            game.render(dt);
            frames++;

            if (timeCount > 1f) {
                window.setTitle("FPS: " + frames);
                frames = 0;
                updates = 0;
                timeCount -= 1.0f;
            }
            window.swapBuffers();
            glfwPollEvents();

            endTime = Time.getTime();
            dt = (float)(endTime - beginTime);
            beginTime = endTime;
            timeCount += dt;

            if (Input.isKeyDown(GLFW_KEY_ESCAPE)) return;
        }
        end();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = new Window(600, 400, "Experimental");
        window.create();
    }

    private void end() {
        game.destroy();
        window.destroy();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Experimental().start();
    }
}