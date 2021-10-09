package org.anotherteam;

import org.anotherteam.game.Game;
import org.anotherteam.game.Input;
import org.anotherteam.render.screen.Screen;
import org.anotherteam.render.window.Window;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

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

        game = new Game();

        glEnable(GL_TEXTURE_2D);
        while (!window.shouldClose()) {
            window.update();
            game.update();
            game.render();
            window.swapBuffers();
            glfwPollEvents();
            if (Input.isKeyDown(GLFW_KEY_ESCAPE)) return;
        }
        end();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        window = new Window(Screen.WIDTH, Screen.HEIGHT, "Experimental");
        window.create();
    }

    private void end() {
        game.destroy();
        window.destroy();
        //glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Experimental().start();
    }
}