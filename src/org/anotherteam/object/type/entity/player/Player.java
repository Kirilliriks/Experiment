package org.anotherteam.object.type.entity.player;

import lombok.NonNull;
import org.anotherteam.Input;
import org.anotherteam.level.Level;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.object.type.entity.EntityState;
import org.anotherteam.util.exception.LifeException;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

public class Player extends EntityObject {

    public Player(@NonNull Vector2i position, @NonNull Level level) {
        super(position, level, "testPlayerAtlas.png", PlayerState.IDLE);
        collider.setBounds(-7, 32, 6, 32);
        collider.setInteractBounds(0, 32, 16, 32);
        sprite.setFrameSize(32, 32);
        speed = 25;
        checkFlip();
    }

    @Override
    public void setDefaultState() {
        setState(PlayerState.IDLE);
    }

    @Override
    public boolean isCanWalk() {
        if (!(stateController.getState() instanceof EntityState)) throw new LifeException("Player holds unknown state");
        return ((EntityState)stateController.getState()).isCanWalk();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float newMove = 0;

        if (Input.isKeyDown(GLFW.GLFW_KEY_W))
            position.y += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_S))
            position.y -= 1;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            newMove -= speed * delta;
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            newMove += speed * delta;
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_E)) {
            level.checkInteract(this);
        }
        if (newMove != 0) {
            moveImpulse.add(newMove, 0);
        } else moveImpulse.set(0, 0);



        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) moveImpulse.x *= 2;
        if (move()) setState(PlayerState.WALK);
    }
}
