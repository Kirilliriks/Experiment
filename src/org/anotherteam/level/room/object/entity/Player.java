package org.anotherteam.level.room.object.entity;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.state.type.PlayerState;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.render.sprite.Sprite;
import org.anotherteam.screen.GameScreen;

public final class Player extends EntityObject {

    public Player(int x, int y) {
        super(x, y, PlayerState.IDLE);
        val atlas = AssetData.getOrLoadSpriteAtlas(AssetData.ENTITY_ATLASES_PATH + "testPlayerAtlas.png", Sprite.SIZE.x, Sprite.SIZE.y);

        spriteController.setSpriteAtlas(atlas);
        collider.setBounds(-6, 0, 6, 32);
        collider.setInteractBounds(0, 0, 16, 32);
        transform.setSpeed(25);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        GameScreen.gameCamera.setPosition(position.x, GameScreen.HEIGHT / 2.0f);

        float newMove = 0;

        if (Input.isKeyPressed(Input.KEY_W)) {
            position.y += 1;
        }
        if (Input.isKeyPressed(Input.KEY_S)) {
            position.y -= 1;
        }
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
