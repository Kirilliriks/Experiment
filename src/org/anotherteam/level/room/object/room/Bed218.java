package org.anotherteam.level.room.object.room;

import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class Bed218 extends StaticObject {

    public Bed218(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        final var asset = AssetData.getOrLoadSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "BED_218.png", 31, 12);
        spriteController.setSpriteAtlas(asset);
        addComponent(spriteController);
    }
}
