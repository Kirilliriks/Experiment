package org.anotherteam.render.window;

import lombok.Getter;
import org.anotherteam.game.Game;
import org.anotherteam.input.Input;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.exception.RenderException;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL42.*;

@Getter
public final class Window {

    private final Input input;
    private final Vector2i aspectRatio;
    private final int[] windowX = new int[1], windowY = new int[1];

    private long handler;

    private int width;
    private int height;
    private String title;

    private boolean vSync;
    private boolean fullscreen;
    private int windowFPSRate;
    private int fpsMax = 165;
    private boolean fpsLocked = true;

    public Window(int width, int height, String title) {
        this.input = new Input(this);
        this.aspectRatio = new Vector2i(16, 9);
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = false;
    }

    public void destroy() {
        glfwSetWindowShouldClose(handler, true);
        glfwFreeCallbacks(handler);
        glfwDestroyWindow(handler);
        glfwTerminate();
    }

    public void swapBuffers() {
        glfwSwapBuffers(handler);
    }

    public void setFullscreen(boolean isFullscreen) {
        this.fullscreen = isFullscreen;

        if (isFullscreen) {
            glfwGetWindowPos(handler, windowX, windowY);
            glfwSetWindowMonitor(handler, glfwGetPrimaryMonitor(), 0, 0, width, height, windowFPSRate);
        } else {
            glfwSetWindowMonitor(handler, 0, windowX[0], windowY[0], width, height, windowFPSRate);
        }
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(handler, title);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handler);
    }

    private void createCallbacks() {
        glfwSetKeyCallback(handler, input.getKeyboard());
        glfwSetCursorPosCallback(handler, input.getMouseMove());
        glfwSetMouseButtonCallback(handler, input.getMouseButton());
        glfwSetScrollCallback(handler, input.getMouseScroll());
        glfwSetWindowSizeCallback(handler, (w, newWidth, newHeight) -> {
            width = newWidth;
            height = newHeight;

            if (Game.getInstance() == null) {
                return;
            }

            Screen.resize();
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

        handler = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, NULL);
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

        GL.createCapabilities(); // CRITICAL

        //glEnable(GL_BLEND);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public int getAspect() {
        return aspectRatio.x;
    }

    public int getRatio() {
        return aspectRatio.y;
    }
}