package org.anotherteam.render.window;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.game.Input;
import org.anotherteam.util.exception.RenderException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {

    private final Input input;

    private long handler;

    private int width;
    private int height;
    private String title;

    private boolean vSync;
    private boolean fullscreen;

    private int[] windowX = new int[1], windowY = new int[1];

    public Window(int width, int height, String title) {
        this.input = new Input();
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = false;
    }

    public void destroy() {
        input.destroy();
        glfwSetWindowShouldClose(handler, true);
        glfwFreeCallbacks(handler);
        glfwDestroyWindow(handler);
        glfwTerminate();
    }

    public void swapBuffers() {
        glfwSwapBuffers(handler);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
        this.fullscreen = isFullscreen;
        if (isFullscreen) {
            GLFW.glfwGetWindowPos(handler, windowX, windowY);
            GLFW.glfwSetWindowMonitor(handler, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(handler, 0, windowX[0], windowY[0], width, height, 0);
        }
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handler);
    }

    private void createCallbacks() {
        glfwSetKeyCallback(handler, input.getKeyboard());
        glfwSetCursorPosCallback(handler, input.getMouseMove());
        glfwSetMouseButtonCallback(handler, input.getMouseButton());
    }

    public void update() { }

    public void create() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handler = glfwCreateWindow(width, height, title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, NULL);
        if (handler == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        createCallbacks();

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (videoMode == null) {
            throw new RenderException("Error with get video mode");
        }

        windowX[0] = (videoMode.width() - width) / 2;
        windowY[0] = (videoMode.height() - height) / 2;
        glfwSetWindowPos(
                handler,
                windowX[0],
                windowY[0]
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(handler);
        // Enable v-sync
        glfwSwapInterval(vSync ? GL_TRUE : GL_FALSE);

        // Make the window visible
        glfwShowWindow(handler);

        setFullscreen(fullscreen);
    }
}