package org.anotherteam.editor.level;

import lombok.val;
import org.anotherteam.Game;
import org.anotherteam.Input;
import org.anotherteam.data.AssetData;
import org.anotherteam.editor.Editor;
import org.anotherteam.editor.gui.GUIElement;
import org.anotherteam.editor.gui.Label;
import org.anotherteam.editor.gui.menu.sprite.SpriteMenu;
import org.anotherteam.editor.gui.menu.text.SwitchButton;
import org.anotherteam.editor.gui.menu.text.TextMenu;
import org.anotherteam.editor.gui.menu.text.SwitchMenu;
import org.anotherteam.editor.render.EditorBatch;
import org.anotherteam.editor.screen.DraggedTile;
import org.anotherteam.level.room.tile.Tile;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.exception.LifeException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class TileViewer extends GUIElement {

    private final SwitchMenu typeMenu;

    private final DraggedTile highliter;
    private DraggedTile draggedTile;

    public TileViewer(float x, float y, GUIElement ownerElement) {
        super(x, y, ownerElement);
        val editor = Editor.getInstance();
        width = (int)(editor.getWidth() - getPosX() - Editor.getRightBorderSize());
        height = (int)(getPosY() - Editor.getDownBorderPos() - Editor.DEFAULT_BORDER_SIZE);
        inverted = true;

        typeMenu = new SwitchMenu(0, 0, width, TextMenu.Type.HORIZONTAL, this);
        typeMenu.setInverted(true);
        typeMenu.setColor(100, 100, 100, 255);
        typeMenu.setStartOffset(Label.DEFAULT_TEXT_OFFSET, 0);
        fillAtlasesButtons();
        typeMenu.setClicked(typeMenu.getButton(0));

        highliter = new DraggedTile(0, 0, AssetData.EDITOR_HIGHLITER_ATLAS);
    }

    public void fillAtlasesButtons() {
        val files = new File(AssetData.ROOM_ATLASES_PATH).listFiles();
        if (files == null) throw new LifeException("Room's atlases not found");

        for (val file : files) {
            val btn = typeMenu.addButton(file.getName());
            generateTileMenu(btn, file.getName());
        }
    }

    public void generateTileMenu(@NotNull SwitchButton button, @NotNull String fileName) {
        val spriteAtlas = AssetData.getSpriteAtlas(AssetData.ROOM_ATLASES_PATH + fileName);

        val spriteMenu = new SpriteMenu(0, -typeMenu.getHeight(), width, height - typeMenu.getHeight(), this);
        spriteMenu.setVisible(false);
        spriteMenu.setInverted(true);
        for (val sprite : spriteAtlas.getSprites()) {
            val y = spriteAtlas.getSizeY() - sprite.getFrameY() - 1;
            if (y < spriteAtlas.getSizeY() / 2) continue;

            val x = sprite.getFrameX();
            val spriteButton = spriteMenu.addButton(x, y - spriteAtlas.getSizeY() / 2, sprite);
            spriteButton.setOnClick(()-> {
                draggedTile = new DraggedTile(x, sprite.getFrameY(), spriteAtlas);
                GameScreen.draggedThing = draggedTile;
            });
        }
        button.setOnClick(()-> spriteMenu.setVisible(true));
        button.setAfterClick(()-> spriteMenu.setVisible(false));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            GameScreen.draggedThing = highliter;
        } else {
            draggedTile = highliter;
        }
    }

    @Override
    public void update(float dt) {
        if (draggedTile != null && draggedTile != highliter) {
            if (Input.isButtonPressed(Input.MOUSE_LEFT_BUTTON)) {
                var x = GameScreen.inGameMouseX();
                var y = GameScreen.inGameMouseY();
                if (x < 0 ||  y < 0) return;

                x /= Tile.SIZE.x;
                y /= Tile.SIZE.y;

                val tile = draggedTile.createTile(x, y);
                Game.game.getCurrentRoom().setTile(tile);
                Editor.sendLogMessage("added " + x + " " + y);
            } else if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                GameScreen.draggedThing = highliter;
                draggedTile = highliter;
            }
            return;
        }
        if (Input.isAnyButtonPressed()) {
            if (Input.isButtonPressed(Input.MOUSE_RIGHT_BUTTON)) {
                val x = GameScreen.onMouseTileX();
                val y = GameScreen.onMouseTileY();
                Game.game.getCurrentRoom().removeTile(x, y);
            }
        }
    }

    @Override
    public void render(@NotNull EditorBatch editorBatch) {
        if (!visible) return;

        super.render(editorBatch);

        if (GameScreen.draggedThing == null || draggedTile == null || draggedTile == highliter) return;
        editorBatch.draw(draggedTile.getSprite(), Input.getMouseX(), Input.getMouseY(), SpriteMenu.ICON_SIZE, SpriteMenu.ICON_SIZE);
    }
}
