package org.anotherteam;

import org.anotherteam.render.window.Window;
import org.anotherteam.util.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

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

        double frameRateDelta = 1.0f / window.fpsMax;

        double beginTime = Time.getTime();
        double endTime;
        float dt;
        float timeCount = 0.0f;
        double unprocessedTime = 0.0f;

        int frames = 0;
        int updates = 0;

        boolean canRender;

        glViewport(0, 0, window.getWidth(), window.getHeight());
        while (!window.shouldClose()) {
            canRender = !window.fpsLocked;

            while (unprocessedTime >= frameRateDelta) {
                unprocessedTime -= frameRateDelta;

                window.update();
                game.update((float) frameRateDelta);
                updates++;

                canRender = true;
                if (!window.fpsLocked) break;
            }

            if (canRender) {
                game.render((float) frameRateDelta);
                frames++;

                glfwSwapBuffers(window.handler);
            }

            if (timeCount > 1f) {
                window.setTitle("FPS: " + frames);
                if (!window.fpsLocked)
                    frameRateDelta = 1.0f / frames;
                frames = 0;
                updates = 0;
                timeCount -= 1.0f;
            }

            endTime = Time.getTime();
            dt = (float)(endTime - beginTime);
            beginTime = endTime;
            timeCount += dt;
            unprocessedTime += dt;

            if (Input.isKeyDown(Input.KEY_ESCAPE)) return;
        }
        end();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = new Window(1920, 1080, "Experimental");
        window.create();
        window.setFullscreen(true);
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