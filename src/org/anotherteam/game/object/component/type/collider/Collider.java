package org.anotherteam.game.object.component.type.collider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.fieldcontroller.FieldController;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.Screen;
import org.anotherteam.util.Color;
import org.anotherteam.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Collider extends AABB {

    private final InteractAABB interactAABB;

    private boolean solid;
    private boolean interactive;

    public Collider() {
        super();
        solid = false;
        interactAABB = new InteractAABB(this);
        serializable = true;
    }

    @Override
    public List<FieldController> getFields() {
        final List<FieldController> list = new ArrayList<>();
        list.add(new FieldController("isSolid", solid, (value) -> solid = (boolean) value));
        list.add(new FieldController("isInteractive", interactive, (value) -> interactive = (boolean) value));
        return list;
    }

    @Override
    public void start() {
        if (!min.equals(max)) return;
        if (!ownerObject.hasComponent(SpriteComponent.class)) {
            setBounds(Sprite.DEFAULT_SIZE.x, Sprite.DEFAULT_SIZE.y);
            return;
        }

        final var sprite = ownerObject.getComponent(SpriteComponent.class);
        final var texture = sprite.getTextureSprite();
        setBounds(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void init(Component component) {
        super.init(component);

        if (component instanceof Collider collider) {
            interactAABB.init(collider.interactAABB);
        }
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        ownerObject.addComponent(interactAABB);
    }

    public void setInteractBounds(int minX, int minY, int maxX, int maxY) {
        interactAABB.setBounds(minX, minY, maxX, maxY);
    }

    public boolean isCollide(@NotNull Collider aabb, @NotNull Vector2f moveVector) {
        return isCollide(aabb, moveVector.x, moveVector.y);
    }

    public boolean isCollide(@NotNull Collider aabb, float x, float y) {
        if (!aabb.isSolid()) return false;
        final var collisionX = objectPosition.x + max.x + x >= aabb.getPosition().x + aabb.getMin().x &&
                objectPosition.x + min.x + x <= aabb.getPosition().x + aabb.getMax().x;

        final var collisionY = objectPosition.y + max.y + y >= aabb.getPosition().y + aabb.getMin().y &&
                objectPosition.y + min.y + y <= aabb.getPosition().y + aabb.getMax().y;
        return collisionX && collisionY;
    }

    public boolean isOnMouse(float x, float y) {
        if (x < 0 || y < 0) return false;

        return objectPosition.x + min.x <= x && x < objectPosition.x + max.x &&
                objectPosition.y + min.y <= y && y < objectPosition.y + max.y;
    }

    public boolean isCanInteract(@NotNull Collider collider) {
        if (!interactive) return false;
        return interactAABB.isInteract(collider);
    }

    public void checkInteract() {
//        for (final var object : ownerObject.getRoom().getGameObjects()) { TODO
//            if (!(object instanceof InteractiveObject)) continue;
//            final var collider = object.getCollider();
//            if (!isCanInteract(collider)) continue;
//            ((InteractiveObject)ownerObject).interactBy(object);
//        }
    }

    public boolean checkCollide(@NotNull Vector2f moveVector) {
        for (final var object : ownerObject.getRoom().getGameObjects()) {
            final var collider = object.getCollider();
            if (!isCollide(collider, moveVector)) continue;
            return true;
        }
        return false;
    }

    public void setFlipX(boolean flip) {
        interactAABB.flip(flip);
    }

    @Override
    public void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch) {
        interactAABB.debugRender(inEditor, debugBatch);

        Color color = Color.RED;
        if (isOnMouse(Screen.inGameMouseX(), Screen.inGameMouseY())) {
            color = Color.GREEN;
        }

        super.debugRender(inEditor, debugBatch, color);
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        result.add("min", SerializeUtil.serialize(min));
        result.add("max", SerializeUtil.serialize(max));
        result.add("solid", new JsonPrimitive(solid));
        result.add("interactive", new JsonPrimitive(interactive));

        result.add("interactMin", SerializeUtil.serialize(interactAABB.getMin()));
        result.add("interactMax", SerializeUtil.serialize(interactAABB.getMax()));
        return result;
    }

    public static Collider deserialize(JsonObject object) {
        final var min = SerializeUtil.deserialize(object.get("min").getAsJsonObject());
        final var max = SerializeUtil.deserialize(object.get("max").getAsJsonObject());
        final var interactMin = SerializeUtil.deserialize(object.get("interactMin").getAsJsonObject());
        final var interactMax = SerializeUtil.deserialize(object.get("interactMax").getAsJsonObject());

        final var collider = new Collider();
        collider.setBounds(min.x, min.y, max.x, max.y);
        collider.setInteractBounds(interactMin.x, interactMin.y, interactMax.x, interactMax.y);
        collider.setSolid(object.get("solid").getAsBoolean());
        collider.setInteractive(object.get("interactive").getAsBoolean());
        return collider;
    }

    public static class InteractAABB extends AABB {

        private final Collider ownerCollider;

        public InteractAABB(@NotNull Collider ownerCollider) {
            super();
            this.ownerCollider = ownerCollider;
            setBounds(ownerCollider.min.x, ownerCollider.min.y, ownerCollider.max.x, ownerCollider.max.y);
            serializable = false;
        }

        public boolean isInteract(@NotNull Collider otherCollider) {
            final var otherInteractAABB = otherCollider.interactAABB;
            return (!((otherCollider.objectPosition.x + otherCollider.max.x) < otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + min.x ||
                    (otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + otherInteractAABB.max.x) < otherCollider.objectPosition.x + otherCollider.min.x));
        }

        public void flip(boolean flip) {
            if (flip) {
                setOffSet(ownerCollider.getMin().x - getMax().x, 0);
            } else {
                setOffSet(ownerCollider.getMax().x, 0);
            }
        }

        @Override
        public void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch) {
            super.debugRender(inEditor, debugBatch, Color.BLUE);
        }
    }
}
