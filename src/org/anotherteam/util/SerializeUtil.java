package org.anotherteam.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.anotherteam.data.deserialization.LevelDeserializer;
import org.anotherteam.data.deserialization.room.RoomDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.ComponentDeserializer;
import org.anotherteam.data.deserialization.room.gameobject.GameObjectDeserializer;
import org.anotherteam.data.deserialization.room.tile.TileDeserializer;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.joml.Vector2i;

public final class SerializeUtil {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Tile.class, new TileDeserializer())
            .registerTypeAdapter(Component.class, new ComponentDeserializer())
            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
            .registerTypeAdapter(Room.class, new RoomDeserializer())
            .registerTypeAdapter(Level.class, new LevelDeserializer())
            .create();

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
