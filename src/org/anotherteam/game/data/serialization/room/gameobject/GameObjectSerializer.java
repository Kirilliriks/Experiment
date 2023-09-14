package org.anotherteam.game.data.serialization.room.gameobject;

import com.google.gson.*;
import org.anotherteam.util.SerializeUtil;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.util.exception.LifeException;

import java.lang.reflect.Type;

public final class GameObjectSerializer implements JsonDeserializer<GameObject>, JsonSerializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final var object = json.getAsJsonObject();
        final var pos = SerializeUtil.deserialize(object.getAsJsonObject("pos"));
        final String name = object.get("name").getAsString();
        final String type = object.get("type").getAsString();

        GameObject gameObject;
        try {
            final Class<?> clazz = Class.forName(type);
            gameObject = GameObject.create(pos.x, pos.y, name, (Class<? extends GameObject>) clazz);
        } catch (ClassNotFoundException e) {
            throw new LifeException("Unknown GameObject type " + type);
        }

        gameObject.setPosition(pos.x, pos.y);
        for (final var componentJSON : object.getAsJsonArray("components")) {
            gameObject.addComponent(context.deserialize(componentJSON, Component.class));
        }

        return gameObject;
    }

    @Override
    public JsonElement serialize(GameObject gameObject, Type typeOfSrc, JsonSerializationContext context) {
        final var result = new JsonObject();
        result.add("name", new JsonPrimitive(gameObject.getName()));
        result.add("type", new JsonPrimitive(gameObject.getClass().getCanonicalName()));
        result.add("pos", SerializeUtil.serialize(gameObject.getPosition()));

        final var components = new JsonArray(gameObject.getComponents().size());
        for (final var component : gameObject.getComponents()) {
            if (!component.isSerializable()) continue;

            components.add(context.serialize(component, Component.class));
        }

        result.add("components", components);
        return result;
    }
}
