package org.anotherteam.oldeditor.screen;

import org.anotherteam.data.AssetData;
import org.anotherteam.oldeditor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.jetbrains.annotations.NotNull;

public final class DraggedTile extends DraggedSprite {

    private final SpriteAtlas spriteAtlas;

    public DraggedTile(int frameX, int frameY, @NotNull SpriteAtlas spriteAtlas) {
        super(spriteAtlas.getTextureSprite(frameX, frameY));
        this.spriteAtlas = spriteAtlas;
    }

    @NotNull
    public Tile createTile(int x, int y) {
        return new Tile(x, y, frameX, frameY, spriteAtlas);
    }

    public void editorRender(float x, float y, @NotNull RenderBatch renderBatch) {
        renderBatch.draw(sprite, x, y, SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);

        if (spriteAtlas != AssetData.EDITOR_HIGHLITER_ATLAS)
            renderBatch.draw(AssetData.EDITOR_HIGHLITER_TEXTURE, x, y, SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);
    }

    @Override
    public void draw(int x, int y, @NotNull RenderBatch renderBatch) {
        final var xF = (x / Tile.SIZE.x) * Tile.SIZE.x;
        final var yF = (y / Tile.SIZE.y) * Tile.SIZE.y;
        renderBatch.draw(sprite, xF, yF, Tile.SIZE.x, Tile.SIZE.y);

        if (spriteAtlas != AssetData.EDITOR_HIGHLITER_ATLAS) {
            renderBatch.draw(AssetData.EDITOR_HIGHLITER_TEXTURE, xF, yF, Tile.SIZE.x, Tile.SIZE.y);
        }
    }

    @Override
    public void debugDraw(int x, int y, boolean inEditor, @NotNull RenderBatch renderBatch) { }
}
