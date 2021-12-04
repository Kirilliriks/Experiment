package org.anotherteam.level.room.object.room;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public class TestCupboard extends StaticObject {

    public TestCupboard(int x, int y) {
        super(x, y);
        val spriteController = new SpriteController(0);
        val asset = AssetData.getOrLoadSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "cupboard_test.png", 20, 36);
        spriteController.setSpriteAtlas(asset);
        addComponent(spriteController);
        collider.setBounds(32, 32);
    }
}
