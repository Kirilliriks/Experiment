package org.anotherteam.render.window;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL42.*;

import org.anotherteam.Input;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.exception.RenderException;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {

    private final Input input;
    private final Vector2i aspectRatio;

    private long handler;

    private int width;
    private int height;
    private String title;

    private boolean vSync;
    private boolean fullscreen;
    private int windowFPSRate;
    private int fpsMax = 120;
    private boolean fpsLocked = true;

    private final int[] windowX = new int[1], windowY = new int[1];

    public Window(int width, int height, String title) {
        this.input = new Input(this);
        this.aspectRatio = new Vector2i(16, 9);
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = false;
        GameScreen.setWindow(this);
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
            GLFW.glfwSetWindowMonitor(handler, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, windowFPSRate);
        } else {
            GLFW.glfwSetWindowMonitor(handler, 0, windowX[0], windowY[0], width, height, windowFPSRate);
        }
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(handler, title);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handler);
    }

    private void createCallbacks() {
        glfwSetKeyCallback(handler, input.getKeyboard());
        glfwSetCursorPosCallback(handler, input.getMouseMove());
        glfwSetMouseButtonCallback(handler, input.getMouseButton());
        glfwSetScrollCallback(handler, input.getMouseScroll());
        glfwSetWindowSizeCallback(handler, (w, newWidth, newHeight) -> {
            width = newWidth;
            height = newHeight;
        });
    }

    public void update() {
        input.tick();
        glfwPollEvents();
    }

    public void create() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        handler = glfwCreateWindow(width, height, title, fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, NULL);
        if (handler == NULL) {
            throw new RenderException("Failed to create the GLFW window");
        }

        createCallbacks();

        final var videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (videoMode == null) {
            throw new RenderException("Error with get video mode");
        }

        windowFPSRate = videoMode.refreshRate();
        glfwWindowHint(GLFW_RED_BITS, videoMode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, videoMode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, videoMode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, videoMode.refreshRate());
        glfwSetWindowSizeCallback(handler, (w, newWidth, newHeight) -> {
            width = newWidth;
            height = newHeight;
        });

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

        //glEnable(GL_BLEND);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public long getHandler() {
        return handler;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFpsMax() {
        return fpsMax;
    }

    public boolean isFpsLocked() {
        return fpsLocked;
    }

    public int getAspect() {
        return aspectRatio.x;
    }

    public int getRatio() {
        return aspectRatio.y;
    }

    @NotNull
    public Vector2i getAspectRatio() {
        return aspectRatio;
    }
}