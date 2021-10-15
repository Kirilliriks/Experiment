package org.anotherteam.data.level.gameobject;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.type.entity.player.Player;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>, JsonSerializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return GameObjectFabric.buildGameObject(json.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(GameObject gameObject, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("type", new JsonPrimitive(gameObject.getClass().getSimpleName()));
        result.add("posX", new JsonPrimitive(gameObject.getPosition().x));
        result.add("posY", new JsonPrimitive(gameObject.getPosition().y));
        return result;
    }

    protected static class GameObjectFabric {

        public static GameObject buildGameObject(@NotNull JsonObject jsonObject) {
            val fullType = jsonObject.get("type").getAsString();
            val typeArray = jsonObject.get("type").getAsString().split("\\.");
            val simpleTypeName = typeArray[typeArray.length - 1];
            switch (simpleTypeName) {
                case "Player" -> {
                    return buildPlayer(jsonObject);
                }
                case "GameObject" -> {
                    return buildObject(jsonObject);
                }
            }
            throw new LifeException("Load unknown entity " + jsonObject.get("type").getAsString());
        }

        private static Player buildPlayer(@NotNull JsonObject playerObject) {
            val posX = playerObject.get("posX").getAsInt();
            val posY = playerObject.get("posY").getAsInt();
            return new Player(posX, posY);
        }

        private static GameObject buildObject(@NotNull JsonObject playerObject) {
            val posX = playerObject.get("posX").getAsInt();
            val posY = playerObject.get("posY").getAsInt();
            return new GameObject(posX, posY);
        }
    }
}
