package org.anotherteam.util;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public final class GameMath {

    public static Vector2f rotate(@NotNull Vector2f vector, float degrees) {
        val newX = vector.x * Math.cos(degrees) - vector.y * Math.sin(degrees);
        val newY = vector.x * Math.sin(degrees) + vector.y * Math.cos(degrees);
        return vector.set(newX, newY);
    }
}
