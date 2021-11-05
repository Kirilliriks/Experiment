package org.anotherteam.editor.screen;

import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.screen.DraggedThing;
import org.jetbrains.annotations.NotNull;

public class DraggedTile extends DraggedThing {

    private SpriteAtlas spriteAtlas;
    private int frameX, frameY;

    public DraggedTile(int frameX, int frameY, @NotNull SpriteAtlas spriteAtlas) {
        super(spriteAtlas.getSprite(frameX, frameY));
        this.frameX = frameX;
        this.frameY = frameY;
        this.spriteAtlas = spriteAtlas;
    }

    @NotNull
    public Tile createTile(int x, int y) {
        return new Tile(x, y, frameX, frameY, spriteAtlas);
    }
}
