package org.anotherteam.level.room.object.room;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.object.type.level.StaticObject;

public final class Lamp218 extends StaticObject {

    public Lamp218(int x, int y) {
        super(x, y);
        val spriteController = new SpriteController(0);
        val asset = AssetData.getOrLoadSpriteAtlas(AssetData.ROOM_OBJECTS_PATH + "LAMP_218.png", 12, 18);
        spriteController.setSpriteAtlas(asset);
        addComponent(spriteController);
    }
}
