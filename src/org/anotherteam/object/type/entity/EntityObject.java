package org.anotherteam.object.type.entity;


import lombok.NonNull;
import org.anotherteam.level.Level;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.state.State;
import org.anotherteam.object.state.StateController;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public abstract class EntityObject extends GameObject {

    private final static int DEFAULT_SPEED = 25;

    protected final StateController stateController;

    protected final Vector2f moveImpulse;

    protected int speed;

    public EntityObject(@NonNull Vector2i position, @NonNull Level level, String texturePath, @NonNull State defaultState) {
        super(position, level);
        this.sprite.loadSprite(texturePath);
        this.moveImpulse = new Vector2f(0, 0);
        this.speed = DEFAULT_SPEED;

        this.drawPriority = 1;

        this.stateController = new StateController(this, sprite, defaultState);
    }

    @Override
    public void onAnimationEnd() {
        stateController.onAnimationEnd();
    }

    public abstract void setDefaultState();

    public void setState(@NonNull State state) {
        stateController.setState(state);
    }

    public boolean move() {
        if (!isCanWalk()) return false;
        if (moveImpulse.equals(0, 0)) {
            setDefaultState();
            return false;
        }
        if (level.checkCollide(this, moveImpulse)) {
            moveImpulse.set(0, 0);
            return false;
        }
        if (moveImpulse.x >= 1 || moveImpulse.x <= -1) {
            checkFlip();
            position.add((int) moveImpulse.x, (int) moveImpulse.y);
            moveImpulse.set(0, 0);
        }
        return true;
    }

    public void checkFlip(){
        boolean flip = (moveImpulse.x < 0);
        sprite.setFlip(flip);
        collider.setFlip(flip);
    }

    public boolean isCanWalk() { return true; }
}
