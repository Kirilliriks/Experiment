package org.anotherteam.data.deserialization.room;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;

import java.lang.reflect.Type;

public final class RoomDeserializer implements JsonDeserializer<Room>, JsonSerializer<Room> {

    @Override
    public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val jsonObject = json.getAsJsonObject();
        val name = jsonObject.get("name").getAsString();
        val room = new Room(name);
        for (val tileJSON : jsonObject.get("tiles").getAsJsonArray()) {
            val tile = (Tile) context.deserialize(tileJSON, Tile.class);
            room.setTile(tile);
        }
        for (val tileJSON : jsonObject.get("gameObjects").getAsJsonArray()) {
            val gameObject = (GameObject) context.deserialize(tileJSON, GameObject.class);
            room.addObject(gameObject);
        }
        return room;
    }

    @Override
    public JsonElement serialize(Room room, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("name", new JsonPrimitive(room.getName()));

        val tiles = new JsonArray(room.getTiles().size());
        for (val tile : room.getTiles()) {
            tiles.add(context.serialize(tile, Tile.class));
        }
        result.add("tiles", tiles);

        val objects = new JsonArray(room.getGameObjects().size());
        for (val object : room.getGameObjects()) {
            objects.add(context.serialize(object, GameObject.class));
        }
        result.add("gameObjects", objects);
        return result;
    }
}
