package org.anotherteam.game.level.room.object.room;

import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.object.component.type.sprite.SpriteController;
import org.anotherteam.game.object.type.level.StaticObject;

public final class TestCupboard extends StaticObject {

    public TestCupboard() {
        super(0, 0);
        final var spriteController = new SpriteController();
        spriteController.setSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "cupboard_test.png", 20, 36);
        addComponent(spriteController);
    }
}
