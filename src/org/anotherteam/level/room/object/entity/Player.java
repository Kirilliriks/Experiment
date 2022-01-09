package org.anotherteam.level.room.object.entity;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.player.PlayerController;
import org.anotherteam.object.component.state.type.PlayerState;
import org.anotherteam.object.type.entity.EntityObject;
import org.anotherteam.render.sprite.Sprite;

public final class Player extends EntityObject {

    private final PlayerController playerController;

    public Player(int x, int y) {
        super(x, y, PlayerState.IDLE);
        val atlas = AssetData.getOrLoadSpriteAtlas(AssetData.ENTITY_PATH + "testPlayerAtlas.png", Sprite.SIZE.x, Sprite.SIZE.y);

        spriteController.setSpriteAtlas(atlas);
        collider.setBounds(-6, 0, 6, 32);
        collider.setInteractBounds(0, 0, 16, 32);
        transform.setSpeed(25);

        playerController = new PlayerController();
        addComponent(playerController);
    }
}
