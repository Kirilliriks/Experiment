package org.anotherteam.data;

import com.google.gson.*;
import org.anotherteam.level.room.Room;

import java.lang.reflect.Type;

public final class RoomDeserializer implements JsonDeserializer<Room>, JsonSerializer<Room> {

    @Override
    public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(Room src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
