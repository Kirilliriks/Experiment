package org.anotherteam.util;

import lombok.experimental.UtilityClass;
import org.lwjgl.glfw.GLFW;

@UtilityClass
public final class Time {
    public static double getTime() {
        return GLFW.glfwGetTime();
    }
}
