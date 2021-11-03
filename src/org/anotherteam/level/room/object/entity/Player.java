package org.anotherteam.level.room.object.entity;

import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.state.type.PlayerState;
import org.anotherteam.object.type.entity.EntityObject;

public class Player extends EntityObject {

    public static Player PLAYER;

    public Player(int x, int y) {
        super(x, y,
                AssetData.TEST_PLAYER_ATLAS,
                PlayerState.IDLE);
        PLAYER = this;
        collider.setBounds(-6, 0, 6, 32);
        collider.setInteractBounds(0, 0, 16, 32);
        transform.setSpeed(25);
        transform.checkFlip();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float newMove = 0;

        if (Input.isKeyPressed(Input.KEY_W))
            position.y += 1;
        if (Input.isKeyPressed(Input.KEY_S))
            position.y -= 1;

        if (Input.isKeyDown(Input.KEY_A)) {
            newMove -= transform.getSpeed() * delta;
        }
        if (Input.isKeyDown(Input.KEY_D)) {
            newMove += transform.getSpeed() * delta;
        }
        if (Input.isKeyDown(Input.KEY_E)) {
            collider.checkInteract();
        }
        if (newMove != 0) {
            transform.moveImpulse.add(newMove * 2, 0);
        } else transform.moveImpulse.set(0, 0);

        if (Input.isKeyDown(Input.KEY_SHIFT)) transform.moveImpulse.x *= 2;
        if (transform.isMoving()) stateController.setState(PlayerState.WALK);

        if (Input.isKeyPressed(Input.KEY_SPACE)) {
            Game.DebugMode = !Game.DebugMode;
        }
    }
}
