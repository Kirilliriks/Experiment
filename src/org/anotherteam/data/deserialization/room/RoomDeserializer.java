package org.anotherteam.data.deserialization.room;

import com.google.gson.*;
import org.anotherteam.level.room.Room;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.object.GameObject;

import java.lang.reflect.Type;

public final class RoomDeserializer implements JsonDeserializer<Room>, JsonSerializer<Room> {

    @Override
    public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final var jsonObject = json.getAsJsonObject();
        final var name = jsonObject.get("name").getAsString();

        final var room = new Room(name);
        for (final var tileJSON : jsonObject.get("tiles").getAsJsonArray()) {
            final var tile = (Tile) context.deserialize(tileJSON, Tile.class);
            room.setTile(tile);
        }

        for (final var tileJSON : jsonObject.get("gameObjects").getAsJsonArray()) {
            final var gameObject = (GameObject) context.deserialize(tileJSON, GameObject.class);
            room.addObject(gameObject);
        }
        return room;
    }

    @Override
    public JsonElement serialize(Room room, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonObject();
        result.add("name", new JsonPrimitive(room.getName()));

        final var tiles = new JsonArray(room.getTiles().size());
        for (final var tile : room.getTiles()) {
            tiles.add(context.serialize(tile, Tile.class));
        }
        result.add("tiles", tiles);

        final var objects = new JsonArray(room.getGameObjects().size());
        for (final var object : room.getGameObjects()) {
            objects.add(context.serialize(object, GameObject.class));
        }
        result.add("gameObjects", objects);
        return result;
    }
}
