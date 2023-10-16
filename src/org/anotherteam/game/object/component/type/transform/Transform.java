package org.anotherteam.game.object.component.type.transform;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.type.StaticComponent;
import org.anotherteam.game.object.component.type.sprite.SpriteComponent;
import org.anotherteam.game.object.component.type.state.StateComponent;
import org.anotherteam.logger.GameLogger;
import org.anotherteam.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Getter
@Setter
public final class Transform extends StaticComponent {

    public static final int DEFAULT_SPEED = 25;

    private StateComponent stateComponent;
    private SpriteComponent sprite;

    private final Vector2i position = new Vector2i();
    private int maxSpeed;
    private int speed;
    private boolean movable = false;

    private final Vector2f moveImpulse = new Vector2f();
    private boolean moving = false;

    public Transform() {
        this(DEFAULT_SPEED);
    }

    public Transform(int maxSpeed) {
        this.maxSpeed = this.speed = maxSpeed;
        this.serializable = true;
    }

    @Override
    public void init() {
        updateFlip();
    }

    @Override
    public void init(Component component) {
        if (component instanceof Transform transform) {
            position.set(transform.position);
            moveImpulse.set(transform.moveImpulse);
            maxSpeed = transform.maxSpeed;
            speed = transform.speed;
            moving = transform.moving;
            movable = transform.movable;
        }
    }

    @Override
    public void update(float dt) {
        if (!isCanMove()) {
            return;
        }

        moving = move();
    }

    @Override
    public void setDependencies() {
        if (sprite != null && stateComponent != null) {
            return;
        }

        stateComponent = getDependsComponent(StateComponent.class);
        sprite = getDependsComponent(SpriteComponent.class);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public boolean move() {
        if (moveImpulse.equals(0, 0)) {
            if (stateComponent == null) {
                GameLogger.log("NAME " + ownerObject.getName());
                return false;
            }

            stateComponent.defaultState();
            return false;
        }

        if (ownerObject.getCollider().checkCollide(moveImpulse)) {
            moveImpulse.set(0, 0);
            return false;
        }

        if (moveImpulse.x >= 1 || moveImpulse.x <= -1) {
            updateFlip();
            position.add((int) moveImpulse.x, (int) moveImpulse.y);
            moveImpulse.set(0, 0);
        }
        return true;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void updateFlip() {
        final boolean flip = (moveImpulse.x < 0);

        if (sprite != null) {
            sprite.setFlipX(flip);
        }

        ownerObject.getCollider().setFlipX(flip);
    }

    public boolean isCanMove() {
//        if (stateComponent.getState() instanceof EntityState entityState) { TODO
//            return entityState.isCanWalk();
//        }

        return movable;
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        result.add("position", SerializeUtil.serialize(position));
        result.add("maxSpeed", new JsonPrimitive(maxSpeed));
        result.add("speed", new JsonPrimitive(speed));
        result.add("movable", new JsonPrimitive(movable));
        return result;
    }

    public static Transform deserialize(JsonObject object) {
        final Vector2i position = SerializeUtil.deserialize(object.getAsJsonObject("position"));
        final var maxSpeed = object.get("maxSpeed").getAsInt();
        final var speed = object.get("speed").getAsInt();
        final var movable = object.get("movable").getAsBoolean();

        final var transform = new Transform(maxSpeed);
        transform.setSpeed(speed);
        transform.setPosition(position.x, position.y);
        transform.setMovable(movable);
        return transform;
    }
}
