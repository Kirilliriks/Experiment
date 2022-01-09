package org.anotherteam.level.room.object.room;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.type.sprite.SpriteController;

public final class DoorCorridor extends GameObject {

    public DoorCorridor(int x, int y) {
        super(x, y);
        val spriteController = new SpriteController();
        val asset = AssetData.getOrLoadSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "door_corridor_room.png", 21, 30);
        spriteController.setSpriteAtlas(asset);
        addComponent(spriteController);
    }
}
