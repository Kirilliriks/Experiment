package org.anotherteam.game.data.serialization.room.tile;

import com.google.gson.*;
import org.anotherteam.game.level.room.tile.Tile;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class TileSerializer implements JsonDeserializer<Tile>, JsonSerializer<Tile> {

    // int x, int y, int frameX, int frameY, String atlasName

    @Override
    @NotNull
    public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final var jsonObject = json.getAsJsonObject();
        return new Tile(jsonObject.get("xPos").getAsInt(),
                        jsonObject.get("yPos").getAsInt(),
                        jsonObject.get("frameX").getAsInt(),
                        jsonObject.get("frameY").getAsInt(),
                        jsonObject.get("atlasName").getAsString());
    }

    @Override
    @NotNull
    public JsonElement serialize(Tile tile, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonObject();
        result.add("xPos", new JsonPrimitive(tile.getPosition().x));
        result.add("yPos", new JsonPrimitive(tile.getPosition().y));
        result.add("frameX", new JsonPrimitive(tile.getFrameX()));
        result.add("frameY", new JsonPrimitive(tile.getFrameY()));
        result.add("atlasName", new JsonPrimitive(tile.getTextureName()));
        return result;
    }
}
