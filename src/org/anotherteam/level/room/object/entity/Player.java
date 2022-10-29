package org.anotherteam.level.room.object.entity;

import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.type.player.PlayerController;
import org.anotherteam.object.component.type.state.type.PlayerState;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.render.sprite.Sprite;

public final class Player extends EntityObject {

    public Player(int x, int y) {
        super(x, y, PlayerState.IDLE);
        spriteController.setSpriteAtlas(AssetData.ENTITY_PATH + "testPlayerAtlas.png", Sprite.SIZE.x, Sprite.SIZE.y);
        getCollider().setBounds(-6, 0, 6, 32);
        getCollider().setInteractBounds(0, 0, 16, 32);
        transform.setSpeed(25);

        PlayerController playerController = new PlayerController();
        addComponent(playerController);
    }
}
