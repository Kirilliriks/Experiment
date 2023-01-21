package org.anotherteam.data.serialization.room.gameobject;

import com.google.gson.*;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.type.collider.Collider;
import org.anotherteam.object.component.type.player.PlayerController;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.component.type.transform.Transform;
import org.anotherteam.util.exception.LifeException;

import java.lang.reflect.Type;

public final class ComponentSerializer implements JsonDeserializer<Component>, JsonSerializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ComponentFabric.deserialize(json);
    }

    @Override
    public JsonElement serialize(Component component, Type typeOfSrc, JsonSerializationContext context) {
        return ComponentFabric.serialize(component);
    }

    public static class ComponentFabric {

        private static JsonElement serialize(Component component) {
            final var result = new JsonObject();
            result.add("type", new JsonPrimitive(component.getClass().getSimpleName()));
            final JsonElement jsonElement = component.serialize(result);

            if (jsonElement == null) {
                throw new LifeException("Unknown component " + result.get("type").getAsString());
            }

            return jsonElement;
        }

        private static Component deserialize(JsonElement json) {
            final var object = json.getAsJsonObject();
            switch (object.get("type").getAsString()) {
                case "Collider" -> {
                    return Collider.deserialize(object);
                }
                case "Transform" -> {
                    return Transform.deserialize(object);
                }
                case "PlayerController" -> {
                    return PlayerController.deserialize(object);
                }
                case "SpriteController" -> {
                    return SpriteController.deserialize(object);
                }
            }
            throw new LifeException("Unknown component " + object.get("type").getAsString());
        }
    }
}
