package org.anotherteam.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.anotherteam.game.data.serialization.LevelSerializer;
import org.anotherteam.game.data.serialization.room.RoomSerializer;
import org.anotherteam.game.data.serialization.room.gameobject.ComponentSerializer;
import org.anotherteam.game.data.serialization.room.gameobject.GameObjectSerializer;
import org.anotherteam.game.data.serialization.room.tile.TileSerializer;
import org.anotherteam.game.level.Level;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.joml.Vector2i;

public final class SerializeUtil {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Tile.class, new TileSerializer())
            .registerTypeAdapter(Component.class, new ComponentSerializer())
            .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
            .registerTypeAdapter(Room.class, new RoomSerializer())
            .registerTypeAdapter(Level.class, new LevelSerializer())
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
