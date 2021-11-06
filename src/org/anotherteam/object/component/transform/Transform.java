package org.anotherteam.object.component.transform;

import lombok.val;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.Collider;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.component.state.StateController;
import org.anotherteam.object.component.state.type.EntityState;
import org.anotherteam.screen.GameScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class Transform extends Component {

    private final int maxSpeed;

    private StateController stateController;
    private Collider collider;
    private SpriteController sprite;

    public final Vector2f moveImpulse;

    private Vector2i position;
    private int speed;

    private boolean moving;

    public Transform(int maxSpeed) {
        this.maxSpeed = maxSpeed;
        moveImpulse = new Vector2f(0.0f, 0.0f);
        speed = maxSpeed;
        moving = false;
        serializable = true;
    }

    @Override
    public void initBy(Component component) {
        val transform = (Transform) component;
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
            val x = (int) moveImpulse.x;
            val y = (int) moveImpulse.y;
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
        val flip = (moveImpulse.x < 0);
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
