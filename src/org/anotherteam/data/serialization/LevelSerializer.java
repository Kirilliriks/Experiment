package org.anotherteam.data.serialization;

import com.google.gson.*;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;

import java.lang.reflect.Type;

public final class LevelSerializer implements JsonDeserializer<Level>, JsonSerializer<Level> {

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final var jsonObject = json.getAsJsonObject();
        final var name = jsonObject.get("name").getAsString();

        final var level = new Level(name);
        for (final var roomJSON : jsonObject.get("rooms").getAsJsonArray()) {
            final var room = (Room) context.deserialize(roomJSON, Room.class);
            level.addRoom(room);
        }
        return level;
    }

    @Override
    public JsonElement serialize(Level level, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonObject();
        result.add("name", new JsonPrimitive(level.getName()));

        final var rooms = new JsonArray(level.getRooms().size());
        for (final var room : level.getRooms()) {
            rooms.add(context.serialize(room, Room.class));
        }
        result.add("rooms", rooms);
        return result;
    }
}
