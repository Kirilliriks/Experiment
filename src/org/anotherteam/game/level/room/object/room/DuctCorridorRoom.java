package org.anotherteam.game.level.room.object.room;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.type.level.StaticObject;

public final class DuctCorridorRoom extends StaticObject {

    public DuctCorridorRoom() {
        super(0, 0);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "duct_corridor_room.png", 31, 49);
        addComponent(spriteController);
    }
}
