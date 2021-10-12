package org.anotherteam.object.type.entity.player;

import lombok.NonNull;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetsData;
import org.anotherteam.level.Level;
import org.anotherteam.object.component.state.player.PlayerState;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.render.sprite.Sprite;
import org.joml.Vector2i;

public class Player extends EntityObject {

    public Player(@NonNull Vector2i position, @NonNull Level level) {
        super(position, level,
                new Sprite(AssetsData.getTexture("testPlayerAtlas.png"),
                        0, 0, 32, 32),
                PlayerState.IDLE);
        collider.setBounds(-7, 32, 6, 32);
        collider.setInteractBounds(0, 32, 16, 32);
        transform.speed = 25;
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
            newMove -= transform.speed * delta;
        }
        if (Input.isKeyDown(Input.KEY_D)) {
            newMove += transform.speed * delta;
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
