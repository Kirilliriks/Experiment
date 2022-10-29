package org.anotherteam.level.room.object.room;

import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class LightTest extends StaticObject {

    public LightTest(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "LIGHT_TEST.png", 32, 32);
        addComponent(spriteController);
    }
}
