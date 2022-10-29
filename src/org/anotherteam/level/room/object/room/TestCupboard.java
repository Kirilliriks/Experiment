package org.anotherteam.level.room.object.room;

import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.type.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class TestCupboard extends StaticObject {

    public TestCupboard(int x, int y) {
        super(x, y);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "cupboard_test.png", 20, 36);
        addComponent(spriteController);
    }
}
