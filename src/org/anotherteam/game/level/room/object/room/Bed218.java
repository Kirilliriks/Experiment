package org.anotherteam.game.level.room.object.room;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.type.level.StaticObject;

public final class Bed218 extends StaticObject {

    public Bed218(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "BED_218.png", 31, 12);
        addComponent(spriteController);
    }
}
