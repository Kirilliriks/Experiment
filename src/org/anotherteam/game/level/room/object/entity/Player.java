package org.anotherteam.game.level.room.object.entity;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.component.type.player.PlayerController;
import org.anotherteam.game.object.component.type.state.type.PlayerState;
import org.anotherteam.game.object.type.entity.EntityObject;
import org.anotherteam.render.sprite.Sprite;

public final class Player extends EntityObject {

    public Player() {
        super(0, 0, PlayerState.IDLE);
        spriteController.setSpriteAtlas(AssetData.ENTITY_PATH + "testPlayerAtlas.png", Sprite.DEFAULT_SIZE.x, Sprite.DEFAULT_SIZE.y);
        getCollider().setBounds(-6, 0, 6, 32);
        getCollider().setInteractBounds(0, 0, 16, 32);
        transform.setSpeed(25);

        PlayerController playerController = new PlayerController();
        addComponent(playerController);
    }
}
