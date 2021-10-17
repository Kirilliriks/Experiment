package org.anotherteam.data.deserialization;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.level.Level;
import org.anotherteam.level.room.Room;

import java.lang.reflect.Type;

public final class LevelDeserializer implements JsonDeserializer<Level>, JsonSerializer<Level> {

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val jsonObject = json.getAsJsonObject();
        val name = jsonObject.get("name").getAsString();
        val level = new Level(name);
        for (val roomJSON : jsonObject.get("rooms").getAsJsonArray()) {
            val room = (Room) context.deserialize(roomJSON, Room.class);
            level.addRoom(room);
        }
        return level;
    }

    @Override
    public JsonElement serialize(Level level, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("name", new JsonPrimitive(level.getName()));
        val rooms = new JsonArray(level.getRooms().size());
        for (val room : level.getRooms()) {
            rooms.add(context.serialize(room, Room.class));
        }
        result.add("rooms", rooms);
        return result;
    }
}
