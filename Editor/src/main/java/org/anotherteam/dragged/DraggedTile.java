package org.anotherteam.dragged;

import org.anotherteam.game.Game;
import org.anotherteam.game.data.AssetData;
import org.anotherteam.game.level.room.Room;
import org.anotherteam.game.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;

public final class DraggedTile extends DraggedSprite {

    private final SpriteAtlas atlas;

    public DraggedTile(Tile tile) {
        super(tile.getTextureSprite());

        this.atlas = AssetData.getOrLoadRoomAtlas(tile.getTextureName());
    }

    public DraggedTile(int frameX, int frameY, @NotNull SpriteAtlas atlas) {
        super(atlas.getTextureSprite(frameX, frameY));

        this.atlas = atlas;
    }

    public void placeTile(int x, int y) {
        final Room room = Game.LEVEL_MANAGER.getCurrentRoom();

        room.setTile(new Tile(x, y, frameX, frameY, atlas));
    }

    @Override
    public void draw(int x, int y, @NotNull RenderBatch renderBatch) {
        final int xF = (x / Tile.SIZE.x) * Tile.SIZE.x;
        final int yF = (y / Tile.SIZE.y) * Tile.SIZE.y;
        renderBatch.draw(sprite, xF, yF, Tile.SIZE.x, Tile.SIZE.y);

        renderBatch.draw(AssetData.EDITOR_HIGHLITER_TEXTURE, xF, yF, Tile.SIZE.x, Tile.SIZE.y);
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, @NotNull RenderBatch renderBatch) { }
}
