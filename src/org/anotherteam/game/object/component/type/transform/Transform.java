package org.anotherteam.game.object.component.type.transform;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import org.anotherteam.game.object.component.Component;
import org.anotherteam.game.object.component.type.collider.Collider;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.component.type.state.StateController;
import org.anotherteam.game.object.component.type.state.type.EntityState;
import org.anotherteam.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Getter
@Setter
public final class Transform extends Component {

    public static final int DEFAULT_SPEED = 25;

    private final Vector2i position;
    public final Vector2f moveImpulse;
    private int maxSpeed;
    private int speed;
    private boolean moving;

    private StateController stateController;
    private Collider collider;
    private SpriteController sprite;

    public Transform() {
        this(DEFAULT_SPEED);
    }

    public Transform(int maxSpeed) {
        this.position = new Vector2i(0, 0);
        this.maxSpeed = maxSpeed;
        this.moveImpulse = new Vector2f(0.0f, 0.0f);
        this.speed = maxSpeed;
        this.moving = false;
        this.serializable = true;
    }

    @Override
    public void init() {
        checkFlip();
    }

    @Override
    public void update(float dt) {
        moving = move();
    }

    @Override
    public void setDependencies() {
        if (sprite != null && collider != null && stateController != null) return;

        collider = getDependsComponent(Collider.class);
        stateController = getDependsComponent(StateController.class);
        sprite = getDependsComponent(SpriteController.class);
    }

    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public boolean move() {
        if (!isCanMove()) return false;

        if (moveImpulse.equals(0, 0)) {
            stateController.setDefaultState();
            return false;
        }

        if (collider.checkCollide(moveImpulse)) {
            moveImpulse.set(0, 0);
            return false;
        }

        if (moveImpulse.x >= 1 || moveImpulse.x <= -1) {
            checkFlip();
            final var x = (int) moveImpulse.x;
            final var y = (int) moveImpulse.y;
            position.add(x, y);
            moveImpulse.set(0, 0);
        }
        return true;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void checkFlip() {
        final var flip = (moveImpulse.x < 0);
        sprite.setFlipX(flip);
        collider.setFlipX(flip);
    }

    public boolean isCanMove() {
        return ((EntityState)stateController.getState()).isCanWalk();
    }

    @Override
    public @NotNull JsonElement serialize(JsonObject result) {
        result.add("posion", SerializeUtil.serialize(position));
        result.add("maxSpeed", new JsonPrimitive(maxSpeed));
        result.add("speed", new JsonPrimitive(speed));
        return result;
    }

    public static Transform deserialize(JsonObject object) {
        final Vector2i position = SerializeUtil.deserialize(object.getAsJsonObject("position"));
        final var maxSpeed = object.get("maxSpeed").getAsInt();
        final var speed = object.get("speed").getAsInt();

        final var transform = new Transform(maxSpeed);
        transform.setSpeed(speed);
        transform.setPosition(position.x, position.y);
        return transform;
    }
}
