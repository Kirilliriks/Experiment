package org.anotherteam.game.level.room.object.room;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.GameObject;
import org.anotherteam.game.object.component.type.sprite.SpriteController;

public final class DoorCorridor extends GameObject {

    public DoorCorridor(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "door_corridor_room.png", 21, 30);
        addComponent(spriteController);
    }
}
