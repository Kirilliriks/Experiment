package org.anotherteam.data.level.gameobject;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.data.level.SerializerUtil;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.AABB;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.util.exception.LifeException;

import java.lang.reflect.Type;

public final class ComponentDeserializer implements JsonDeserializer<Component>, JsonSerializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(Component component, Type typeOfSrc, JsonSerializationContext context) {
        return ComponentFabric.serialize(component);
    }

    private static class ComponentFabric {

        private static Component deserialize(JsonElement json) {
            val object = json.getAsJsonObject();
            switch (object.get("type").getAsString()) {
                case "Collider" -> {
                   return deserialize(object);
                }
            }
            throw new LifeException("Unknown component " + object.get("type").getAsString());
        }

        private static JsonElement serialize(Component component) {
            val result = new JsonObject();
            result.add("type", new JsonPrimitive(component.getClass().getSimpleName()));
            switch (result.get("type").getAsString()) {
                case "Collider" -> {
                    return serialize(result, (Collider) component);
                }
            }
            throw new LifeException("Unknown component " + result.get("type").getAsString());
        }

        public static JsonElement serialize(JsonObject result, Collider collider) {
            result.add("firstBound", SerializerUtil.serialize(collider.getFirstBound()));
            result.add("secondBound", SerializerUtil.serialize(collider.getSecondBound()));
            result.add("solid", new JsonPrimitive(collider.isSolid()));
            result.add("interactive", new JsonPrimitive(collider.isInteractive()));
            val interactAABB = (AABB) collider.getInteractAABB();
            result.add("interactFirstBound", SerializerUtil.serialize(interactAABB.getFirstBound()));
            result.add("interactSecondBound", SerializerUtil.serialize(interactAABB.getSecondBound()));
            return result;
        }

        public static Collider deserialize(JsonObject object) {
            val firstBound = SerializerUtil.deserialize(object.get("firstBound").getAsJsonObject());
            val secondBound = SerializerUtil.deserialize(object.get("secondBound").getAsJsonObject());
            val interactFirstBound = SerializerUtil.deserialize(object.get("interactFirstBound").getAsJsonObject());
            val interactSecondBound = SerializerUtil.deserialize(object.get("interactSecondBound").getAsJsonObject());
            val collider = new Collider();
            collider.setBounds(firstBound.x, firstBound.y, secondBound.x, secondBound.y);
            collider.setInteractBounds(interactFirstBound.x, interactFirstBound.y, interactSecondBound.x, interactSecondBound.y);
            collider.setSolid(object.get("solid").getAsBoolean());
            collider.setInteractive(object.get("interactive").getAsBoolean());
            return collider;
        }
    }
}
