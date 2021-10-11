package org.anotherteam.object.component.transform;

import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.Component;
import org.anotherteam.object.component.collider.ColliderComponent;
import org.anotherteam.object.component.sprite.SpriteComponent;
import org.anotherteam.object.component.state.StateController;
import org.anotherteam.object.type.entity.EntityState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class TransformComponent extends Component {

    private final int maxSpeed;

    private StateController stateController;
    private ColliderComponent collider;
    private SpriteComponent sprite;

    public final Vector2f moveImpulse;

    private Vector2i ownerPosition;
    public int speed;

    private boolean moving;

    public TransformComponent(int maxSpeed) {
        this.maxSpeed = maxSpeed;
        moveImpulse = new Vector2f(0.0f, 0.0f);
        speed = maxSpeed;
        moving = false;
    }

    @Override
    public void update(float dt) {
        moving = move();
    }

    @Override
    public void setOwnerObject(@NotNull GameObject ownerObject) {
        super.setOwnerObject(ownerObject);
        ownerPosition = ownerObject.getPosition();
    }

    @Override
    public void setDependencies() {
        if (sprite != null && collider != null && stateController != null) return;
        collider = getDependsComponent(ColliderComponent.class);
        stateController = getDependsComponent(StateController.class);
        sprite = getDependsComponent(SpriteComponent.class);
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
            ownerPosition.add((int) moveImpulse.x, (int) moveImpulse.y);
            moveImpulse.set(0, 0);
        }
        return true;
    }

    public void checkFlip() {
        boolean flip = (moveImpulse.x < 0);
        sprite.setFlip(flip);
        collider.setFlip(flip);
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isCanMove() {
        return ((EntityState)stateController.getState()).isCanWalk();
    }
}
