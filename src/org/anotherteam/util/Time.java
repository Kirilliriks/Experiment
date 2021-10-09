package org.anotherteam.util;

import org.lwjgl.glfw.GLFW;

public final class Time {
    public static double getTime() {
        return GLFW.glfwGetTime();
    }
}
