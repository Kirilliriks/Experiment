package org.anotherteam.data.level.gameobject;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.data.level.SerializerUtil;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.component.transform.Transform;
import org.anotherteam.object.type.entity.player.Player;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>, JsonSerializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return GameObjectFabric.deserialize(json.getAsJsonObject(), context);
    }

    @Override
    public JsonElement serialize(GameObject gameObject, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("type", new JsonPrimitive(gameObject.getClass().getSimpleName()));
        result.add("pos", SerializerUtil.serialize(gameObject.getPosition()));
        val components = new JsonArray(gameObject.getComponents().values().size());
        for (val component : gameObject.getComponents().values()) {
            if (!component.isSerializable()) continue;
            components.add(context.serialize(component, Component.class));
        }
        result.add("components", components);
        return result;
    }

    private static class GameObjectFabric {

        public static GameObject deserialize(@NotNull JsonObject jsonObject, JsonDeserializationContext context) {
            switch (jsonObject.get("type").getAsString()) {
                case "Player" -> {
                    return deserializePlayer(jsonObject, context);
                }
                case "GameObject" -> {
                    return deserializeGameObject(jsonObject, context);
                }
            }
            throw new LifeException("Load unknown entity " + jsonObject.get("type").getAsString());
        }

        private static Player deserializePlayer(@NotNull JsonObject object, JsonDeserializationContext context) {
            val pos = SerializerUtil.deserialize(object.getAsJsonObject("pos"));
            val player = new Player(pos.x, pos.y);
            for (val componentJSON : object.getAsJsonArray("components")) {
                player.addComponent(context.deserialize(componentJSON, Component.class));
            }
            return player;
        }

        private static GameObject deserializeGameObject(@NotNull JsonObject object, JsonDeserializationContext context) {
            val pos = SerializerUtil.deserialize(object.getAsJsonObject("pos"));
            val gameObject = new GameObject(pos.x, pos.y);
            for (val componentJSON : object.getAsJsonArray("components")) {
                gameObject.addComponent(context.deserialize(componentJSON, Component.class));
            }
            return gameObject;
        }
    }
}
