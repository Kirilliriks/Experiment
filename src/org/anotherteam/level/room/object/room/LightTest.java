package org.anotherteam.level.room.object.room;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class LightTest extends StaticObject {

    public LightTest(int x, int y) {
        super(x, y);
        val spriteController = new SpriteController(0);
        val asset = AssetData.getOrLoadSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "LIGHT_TEST.png", 32, 32);
        spriteController.setSpriteAtlas(asset);
        addComponent(spriteController);
    }
}
