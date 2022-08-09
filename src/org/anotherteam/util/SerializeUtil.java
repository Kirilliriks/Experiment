package org.anotherteam.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.joml.Vector2i;

public final class SerializeUtil {

    public static JsonObject serialize(Vector2i vector2i) {
        final var result = new JsonObject();
        result.add("x", new JsonPrimitive(vector2i.x));
        result.add("y", new JsonPrimitive(vector2i.y));
        return result;
    }

    public static Vector2i deserialize(JsonObject object) {
        final var result = new Vector2i();
        result.x = object.get("x").getAsInt();
        result.y = object.get("y").getAsInt();
        return result;
    }
}
