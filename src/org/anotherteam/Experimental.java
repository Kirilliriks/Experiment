package org.anotherteam;

import lombok.val;
import org.anotherteam.editor.Editor;
import org.anotherteam.render.window.Window;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public final class Experimental implements Runnable {

    private Thread thread;
    private Game game;
    public  Editor editor;
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
        editor = new Editor();
        game.init();
        editor.init(Game.levelManager.getCurrentLevel().getName());

        double frameRateDelta = 1.0f / window.getFpsMax();

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
            canRender = !window.isFpsLocked();

            val dtF = (float) frameRateDelta;
            while (unprocessedTime >= frameRateDelta) {
                unprocessedTime -= frameRateDelta;

                window.update();
                if (editor != null)
                    editor.update(dtF);
                game.update(dtF);
                updates++;

                canRender = true;
                if (!window.isFpsLocked()) break;
            }

            if (canRender) {
                game.render(dtF);
                if (editor != null && Game.stateManager.getState() == GameState.ON_EDITOR)
                    editor.renderFrame(GameScreen.windowBatch);
                frames++;

                window.swapBuffers();
            }

            if (timeCount > 1f) {
                window.setTitle("FPS: " + frames);
                if (!window.isFpsLocked())
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

            if (Game.stateManager.getState() == GameState.ON_CLOSE_GAME) return;
        }
        end();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = new Window(1920, 1080, "Experimental");
        window.create();
    }

    private void end() {
        if (editor != null)
            editor.destroy();
        game.destroy();
        window.destroy();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Experimental().start();
    }
}