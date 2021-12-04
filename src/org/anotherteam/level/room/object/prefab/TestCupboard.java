package org.anotherteam.level.room.object.prefab;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.object.GameObject;
import org.anotherteam.object.component.sprite.SpriteController;
import org.anotherteam.render.sprite.Sprite;

public class TestCupboard extends GameObject {
    public TestCupboard(int x, int y) {
        super(x, y);
        val spriteController = new SpriteController(1);
        spriteController.setSpriteAtlas(AssetData.getOrLoadSpriteAtlas("cupboard_test", Sprite.SIZE.x, Sprite.SIZE.y));
        addComponent(spriteController);
    }
}
