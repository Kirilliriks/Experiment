package org.anotherteam.data.deserialization.room.gameobject;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.util.SerializeUtil;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.util.exception.LifeException;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject>, JsonSerializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        val object = json.getAsJsonObject();
        val pos = SerializeUtil.deserialize(object.getAsJsonObject("pos"));

        GameObject gameObject;
        try {
            final Class<?> clazz = Class.forName(object.get("type").getAsString());
            gameObject = GameObject.create(pos.x, pos.y, (Class<? extends GameObject>) clazz);
        } catch (ClassNotFoundException e) {
            throw new LifeException("Unknown GameObject type " + object.get("type").getAsString());
        }

        gameObject.setPosition(pos.x, pos.y);
        for (val componentJSON : object.getAsJsonArray("components")) {
            gameObject.addComponent(context.deserialize(componentJSON, Component.class));
        }
        return gameObject;
    }

    @Override
    public JsonElement serialize(GameObject gameObject, Type typeOfSrc, JsonSerializationContext context) {
        val result = new JsonObject();
        result.add("type", new JsonPrimitive(gameObject.getClass().getCanonicalName()));
        result.add("pos", SerializeUtil.serialize(gameObject.getPosition()));
        val components = new JsonArray(gameObject.getComponents().values().size());
        for (val component : gameObject.getComponents().values()) {
            if (!component.isSerializable()) continue;
            components.add(context.serialize(component, Component.class));
        }
        result.add("components", components);
        return result;
    }
}
