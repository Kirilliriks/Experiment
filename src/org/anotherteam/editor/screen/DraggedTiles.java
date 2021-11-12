package org.anotherteam.editor.screen;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.render.batch.RenderBatch;
import org.anotherteam.render.sprite.SpriteAtlas;
import org.anotherteam.screen.DraggedThing;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DraggedTiles extends DraggedThing {

    private final SpriteAtlas spriteAtlas;
    private final DraggedTile firstTile;

    //TODO remove DraggedTile and use correct class (not DraggedThing child)
    private final List<DraggedTile> tiles;

    public DraggedTiles(@NotNull DraggedTile firstTile) {
        this.firstTile = firstTile;
        spriteAtlas = firstTile.getSprite().getSpriteAtlas();
        tiles = new ArrayList<>();
        tiles.add(firstTile);
    }

    public void placeTiles(int x, int y) {
        val room = Game.game.getCurrentRoom();
        for (val tile : tiles) {
            val xOffset = tile.frameX - firstTile.frameX;
            val yOffset = tile.frameY - firstTile.frameY;
            room.setTile(tile.createTile(x + xOffset, y + yOffset));
        }
    }

    public void fillTiles(int xTile, int yTile) {
        val x1 = Math.min(firstTile.getFrameX(), xTile);
        val x2 = Math.max(firstTile.getFrameX(), xTile);
        val y1 = Math.min(firstTile.getFrameY(), yTile);
        val y2 = Math.max(firstTile.getFrameY(), yTile);
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if (x == firstTile.frameX && y == firstTile.frameY) continue;
                tiles.add(new DraggedTile(x, y, spriteAtlas));
            }
        }
    }

    public void editorRender(float x, float y, @NotNull EditorBatch editorBatch) {
        for (val tile : tiles) {
            val xOffset = (tile.frameX - firstTile.frameX) * SpriteMenu.ICON_SIZE;
            val yOffset = (tile.frameY - firstTile.frameY) * SpriteMenu.ICON_SIZE;
            tile.editorRender(x + xOffset, y + yOffset, editorBatch);
        }
    }

    @Override
    public void render(int x, int y, @NotNull RenderBatch renderBatch) {
        for (val tile : tiles) {
            val xOffset = (tile.frameX - firstTile.frameX) * Tile.SIZE.x;
            val yOffset = (tile.frameY - firstTile.frameY) * Tile.SIZE.y;
            tile.render(x + xOffset, y + yOffset, renderBatch);
        }
    }
}
