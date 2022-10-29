package org.anotherteam.level.room.object.room;

import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class DuctCorridorRoom extends StaticObject {

    public DuctCorridorRoom(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "duct_corridor_room.png", 31, 49);
        addComponent(spriteController);
    }
}
