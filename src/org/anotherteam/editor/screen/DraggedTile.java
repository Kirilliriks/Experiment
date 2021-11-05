package org.anotherteam.editor.screen;

import lombok.val;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.screen.DraggedThing;
import org.jetbrains.annotations.NotNull;

public final class DraggedTile extends DraggedThing {

    private final SpriteAtlas spriteAtlas;
    private final int frameX, frameY;

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

    @Override
    public void render(int x, int y, @NotNull RenderBatch renderBatch) {
        val xF = (x / Tile.SIZE.x) * Tile.SIZE.x;
        val yF = (y / Tile.SIZE.y) * Tile.SIZE.y;
        renderBatch.draw(sprite, xF, yF, Tile.SIZE.x, Tile.SIZE.y);

        if (spriteAtlas != AssetData.EDITOR_HIGHLITER_ATLAS)
            renderBatch.draw(AssetData.EDITOR_HIGHLITER_TEXTURE, xF, yF, Tile.SIZE.x, Tile.SIZE.y);
    }
}
