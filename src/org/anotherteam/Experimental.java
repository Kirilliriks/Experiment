package org.anotherteam;

import imgui.ImGui;
import org.anotherteam.editor.Editor;
import org.anotherteam.render.window.Window;
import org.anotherteam.util.Time;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengles.GLES20.GL_COLOR_BUFFER_BIT;

public final class Experimental {

    private final Game game;
    private final Editor editor;
    private final Window window;

    public Experimental() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window(1920, 1080, "Experimental");
        window.create();

        GL.createCapabilities(); // CRITICAL

        game = new Game(window);
        editor = new Editor(game);
        game.init();

        run();
    }

    public void run() {

        if (editor != null) {
            editor.init();
            Input.imGuiIO = ImGui.getIO();
        }

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

            final var dtF = (float) frameRateDelta;
            while (unprocessedTime >= frameRateDelta) {
                unprocessedTime -= frameRateDelta;

                window.update();
                game.update(dtF);
                updates++;

                canRender = true;
                if (!window.isFpsLocked()) break;
            }

            if (canRender) {

                glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT);
                game.render(dtF);
                if (editor != null) {
                    editor.render(dtF);
                }

                frames++;

                window.swapBuffers();
            }

            if (timeCount > 1f) {
                window.setTitle("FPS: " + frames);
                if (!window.isFpsLocked()) {
                    frameRateDelta = 1.0f / frames;
                }

                frames = 0;
                updates = 0;
                timeCount -= 1.0f;
            }

            endTime = Time.getTime();
            dt = (float)(endTime - beginTime);
            beginTime = endTime;
            timeCount += dt;
            unprocessedTime += dt;

            if (Game.STATE_MANAGER.getState() == GameState.ON_CLOSE_GAME) break;
        }
        end();
    }

    private void end() {
        if (editor != null) {
            editor.destroy();
        }
        game.destroy();
        window.destroy();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Experimental();
    }
}