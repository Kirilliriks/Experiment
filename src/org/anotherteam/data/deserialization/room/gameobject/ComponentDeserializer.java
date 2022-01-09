package org.anotherteam.data.deserialization.room.gameobject;

import com.google.gson.*;
import lombok.val;
import org.anotherteam.util.SerializeUtil;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.type.collider.AABB;
import org.anotherteam.object.component.type.collider.Collider;
import org.anotherteam.object.component.type.transform.Transform;
import org.anotherteam.util.exception.LifeException;

import java.lang.reflect.Type;

public final class ComponentDeserializer implements JsonDeserializer<Component>, JsonSerializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ComponentFabric.deserialize(json);
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
                   return deserializeCollider(object);
                }
                case "Transform" -> {
                    return deserializeTransform(object);
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
                case "Transform" -> {
                    return serialize(result, (Transform) component);
                }
            }
            throw new LifeException("Unknown component " + result.get("type").getAsString());
        }


        public static Collider deserializeCollider(JsonObject object) {
            val firstBound = SerializeUtil.deserialize(object.get("firstBound").getAsJsonObject());
            val secondBound = SerializeUtil.deserialize(object.get("secondBound").getAsJsonObject());
            val interactFirstBound = SerializeUtil.deserialize(object.get("interactFirstBound").getAsJsonObject());
            val interactSecondBound = SerializeUtil.deserialize(object.get("interactSecondBound").getAsJsonObject());
            val collider = new Collider();
            collider.setBounds(firstBound.x, firstBound.y, secondBound.x, secondBound.y);
            collider.setInteractBounds(interactFirstBound.x, interactFirstBound.y, interactSecondBound.x, interactSecondBound.y);
            collider.setSolid(object.get("solid").getAsBoolean());
            collider.setInteractive(object.get("interactive").getAsBoolean());
            return collider;
        }

        public static JsonElement serialize(JsonObject result, Collider collider) {
            result.add("firstBound", SerializeUtil.serialize(collider.getFirstBound()));
            result.add("secondBound", SerializeUtil.serialize(collider.getSecondBound()));
            result.add("solid", new JsonPrimitive(collider.isSolid()));
            result.add("interactive", new JsonPrimitive(collider.isInteractive()));
            val interactAABB = (AABB) collider.getInteractAABB();
            result.add("interactFirstBound", SerializeUtil.serialize(interactAABB.getFirstBound()));
            result.add("interactSecondBound", SerializeUtil.serialize(interactAABB.getSecondBound()));
            return result;
        }

        public static Transform deserializeTransform(JsonObject object) {
            val maxSpeed = object.get("maxSpeed").getAsInt();
            val speed = object.get("speed").getAsInt();
            val transform = new Transform(maxSpeed);
            transform.setSpeed(speed);
            return transform;
        }

        public static JsonElement serialize(JsonObject result, Transform transform) {
            result.add("maxSpeed", new JsonPrimitive(transform.getMaxSpeed()));
            result.add("speed", new JsonPrimitive(transform.getSpeed()));
            return result;
        }
    }
}
