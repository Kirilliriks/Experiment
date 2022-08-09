package org.anotherteam.object.component.type.transform;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.type.collider.Collider;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.component.type.state.StateController;
import org.anotherteam.object.component.type.state.type.EntityState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Transform extends Component {

    public static final int DEFAULT_SPEED = 25;

    private int maxSpeed;

    private StateController stateController;
    private Collider collider;
    private SpriteController sprite;

    public final Vector2f moveImpulse;

    /**
     * Link to GameObject's position
     */
    private Vector2i position;
    private int speed;

    private boolean moving;

    public Transform() {
        this(DEFAULT_SPEED);
    }

    public Transform(int maxSpeed) {
        this.maxSpeed = maxSpeed;
        moveImpulse = new Vector2f(0.0f, 0.0f);
        speed = maxSpeed;
        moving = false;
        serializable = true;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void init() {
        checkFlip();
    }

    @Override
    public void instanceBy(Component component) {
        final var transform = (Transform) component;
        speed = transform.speed;
    }

    @Override
    public void update(float dt) {
        moving = move();
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        position = ownerObject.getPosition();
    }

    @Override
    public void setDependencies() {
        if (sprite != null && collider != null && stateController != null) return;

        collider = getDependsComponent(Collider.class);
        stateController = getDependsComponent(StateController.class);
        sprite = getDependsComponent(SpriteController.class);
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

    public int getSpeed() {
        return speed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void checkFlip() {
        final var flip = (moveImpulse.x < 0);
        sprite.setFlipX(flip);
        collider.setFlipX(flip);
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isCanMove() {
        return ((EntityState)stateController.getState()).isCanWalk();
    }
}
