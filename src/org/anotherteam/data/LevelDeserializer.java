package org.anotherteam.data;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.level.Level;

import java.lang.reflect.Type;

public final class LevelDeserializer implements JsonDeserializer<Level>, JsonSerializer<Level> {

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Level level, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("type", new JsonPrimitive(level.getClass().getCanonicalName()));
        result.add("properties", context.serialize(level, level.getClass()));
        return result;
    }
}
