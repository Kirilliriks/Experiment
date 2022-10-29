package org.anotherteam.object.component.type.collider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.anotherteam.debug.DebugBatch;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.fieldcontroller.FieldController;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.type.level.InteractiveObject;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.anotherteam.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

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
    public void init() {
        if (!firstBound.equals(secondBound)) return;
        if (!ownerObject.hasComponent(SpriteController.class)) {
            setBounds(Sprite.SIZE.x, Sprite.SIZE.y);
            return;
        }

        final var sprite = ownerObject.getComponent(SpriteController.class);
        final var texture = sprite.getTextureSprite();
        setBounds(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        ownerObject.addComponent(interactAABB);
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public void setInteractBounds(int firstX, int firstY, int secondX, int secondY) {
        interactAABB.setBounds(firstX, firstY, secondX, secondY);
    }

    @NotNull
    public InteractAABB getInteractAABB() {
        return interactAABB;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public boolean isCollide(@NotNull Collider aabb, @NotNull Vector2f moveVector){
        return isCollide(aabb, moveVector.x, moveVector.y);
    }

    public boolean isCollide(@NotNull Collider aabb, float x, float y){
        if (!aabb.isSolid()) return false;
        final var collisionX =  objectPosition.x + secondBound.x + x >= aabb.getPosition().x  + aabb.getFirstBound().x &&
                objectPosition.x + firstBound.x + x <= aabb.getPosition().x  + aabb.getSecondBound().x;

        final var collisionY =  objectPosition.y + secondBound.y + y >= aabb.getPosition().y  + aabb.getFirstBound().y &&
                objectPosition.y + firstBound.y + y <= aabb.getPosition().y  + aabb.getSecondBound().y;
        return collisionX && collisionY;
    }

    public boolean isOnMouse(float x, float y) {
        if (x < 0 || y < 0) return false;

        return objectPosition.x + firstBound.x <= x && x <= objectPosition.x + secondBound.x &&
               objectPosition.y + firstBound.y <= y && y <= objectPosition.y + secondBound.y;
    }

    public boolean isCanInteract(@NotNull Collider collider){
        if (!interactive) return false;
        return interactAABB.isInteract(collider);
    }

    public void checkInteract() {
        for (final var object : ownerObject.getRoom().getGameObjects()) {
            if (!(object instanceof InteractiveObject)) continue;
            final var collider = object.getCollider();
            if (!isCanInteract(collider)) continue;
            ((InteractiveObject)ownerObject).interactBy(object);
        }
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
        if (isOnMouse(GameScreen.inGameMouseX(), GameScreen.inGameMouseY())) {
            color = Color.GREEN;
        }

        super.debugRender(inEditor, debugBatch, color);
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        result.add("firstBound", SerializeUtil.serialize(firstBound));
        result.add("secondBound", SerializeUtil.serialize(secondBound));
        result.add("solid", new JsonPrimitive(solid));
        result.add("interactive", new JsonPrimitive(interactive));

        result.add("interactFirstBound", SerializeUtil.serialize(interactAABB.getFirstBound()));
        result.add("interactSecondBound", SerializeUtil.serialize(interactAABB.getSecondBound()));
        return result;
    }

    public static Collider deserialize(JsonObject object) {
        final var firstBound = SerializeUtil.deserialize(object.get("firstBound").getAsJsonObject());
        final var secondBound = SerializeUtil.deserialize(object.get("secondBound").getAsJsonObject());
        final var interactFirstBound = SerializeUtil.deserialize(object.get("interactFirstBound").getAsJsonObject());
        final var interactSecondBound = SerializeUtil.deserialize(object.get("interactSecondBound").getAsJsonObject());

        final var collider = new Collider();
        collider.setBounds(firstBound.x, firstBound.y, secondBound.x, secondBound.y);
        collider.setInteractBounds(interactFirstBound.x, interactFirstBound.y, interactSecondBound.x, interactSecondBound.y);
        collider.setSolid(object.get("solid").getAsBoolean());
        collider.setInteractive(object.get("interactive").getAsBoolean());
        return collider;
    }

    public static class InteractAABB extends AABB {

        private final Collider ownerCollider;

        public InteractAABB(@NotNull Collider ownerCollider) {
            super();
            this.ownerCollider = ownerCollider;
            setBounds(ownerCollider.firstBound.x, ownerCollider.firstBound.y, ownerCollider.secondBound.x, ownerCollider.secondBound.y);
            serializable = false;
        }

        public boolean isInteract(@NotNull Collider otherCollider) {
            final var otherInteractAABB = otherCollider.interactAABB;
            return  (!((otherCollider.objectPosition.x + otherCollider.secondBound.x) < otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + firstBound.x ||
                    (otherInteractAABB.objectPosition.x + otherInteractAABB.offSet.x + otherInteractAABB.secondBound.x) < otherCollider.objectPosition.x  + otherCollider.firstBound.x));
        }

        public void flip(boolean flip) {
            if (flip) {
                setOffSet(ownerCollider.getFirstBound().x - getSecondBound().x, 0);
            } else {
                setOffSet(ownerCollider.getSecondBound().x, 0);
            }
        }

        @Override
        public void debugRender(boolean inEditor, @NotNull DebugBatch debugBatch) {
            super.debugRender(inEditor, debugBatch, Color.BLUE);
        }
    }
}
